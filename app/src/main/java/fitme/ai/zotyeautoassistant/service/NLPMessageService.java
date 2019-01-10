package fitme.ai.zotyeautoassistant.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.speech.ISSErrors;
import com.iflytek.speech.libisssr;
import com.iflytek.speech.sr.ISRListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import fitme.ai.zotyeautoassistant.MyApplication;
import fitme.ai.zotyeautoassistant.bean.DictionaryBean;
import fitme.ai.zotyeautoassistant.bean.ResultBean;
import fitme.ai.zotyeautoassistant.utils.DictionaryGetUtils;
import fitme.ai.zotyeautoassistant.utils.FlightControlContants;
import fitme.ai.zotyeautoassistant.utils.IAppendAudio;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.PinyinDemo;
import fitme.ai.zotyeautoassistant.utils.ResultDealUtils;
import fitme.ai.zotyeautoassistant.utils.SSRecorder;
import fitme.ai.zotyeautoassistant.utils.SoundPlayUtils;
import fitme.ai.zotyeautoassistant.utils.UDPSocketCommand;
import static com.iflytek.speech.libisssr.ISS_SR_MODE_LOCAL_REC;
import static fitme.ai.zotyeautoassistant.MainActivity.byteMerger;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_RESPONSE;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_DEFAULT;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_ERROR;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_RESPONSE_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_SPEECH_END;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_SPEECH_START;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_SPEECH_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_VOLUME;
import static fitme.ai.zotyeautoassistant.utils.Constants.AWAIT_WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOGIN_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOG_LOCAL;
import static fitme.ai.zotyeautoassistant.utils.Constants.TAG;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_CONTROL;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_START;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_TEXT;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP_STATE;
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_COMMAND_0;
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_COMMAND_1;

public class NLPMessageService extends Service implements IAppendAudio {

    private MBroadcastReceiver mBroadcastReceiver;
    private ExecutorService executorService;

    private float[] context_Pe = ResultDealUtils.getContextPe(this);
    private float[] query_Pe = ResultDealUtils.getQuery_tPe(this);
    private TensorFlowInferenceInterface tensorFlowInferenceSlot;
    private TensorFlowInferenceInterface tensorFlowInferenceIntent;
    private DictionaryBean dictionaryBean;


    int err = ISSErrors.ISS_SUCCESS;
    private int currentVolume = 0;
    private boolean waveReturnZero = true;
    private Handler handlerVolume = new Handler();
    private Runnable taskVolume = new Runnable() {
        @Override
        public void run() {
            if (waveReturnZero){
                sendBroadcast(ASR_VOLUME,currentVolume);
                waveReturnZero = false;
            }else {
                sendBroadcast(ASR_VOLUME,0);
                waveReturnZero = true;
            }
            handlerVolume.postDelayed(this,200);
        }
    };

    private int time = 0;
    private Handler fvHandler = new Handler();
    private Runnable fvTask = new Runnable() {
        @Override
        public void run() {
            if (time<10){
                fvHandler.postDelayed(this,1000);
                time++;
            }else {
                //关闭floatingView
                MyApplication.getInstance().getmFloatingView().hide();
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initPresenter();
        //注册广播
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        registerReceiver(mBroadcastReceiver,intentFilter);
        executorService = Executors.newCachedThreadPool(); //线程池
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                initTensorFlow();
            }
        });
        L.i("创建NLPMessageService");
        initAsr();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        L.i("销毁NLPMessageService");
        err = libisssr.stop();
        err = libisssr.destroy();
    }

    //首次激活需要设置机器码，序列号。并且需要联网。从网络获取激活码，进行激活。激活成功后进行初始化
    private String mMechineCode = "test_speechsuite_machinecode";
    private void initAsr(){
        final String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iflytek/res/sr/";
        new Thread(){
            @Override
            public void run() {
                super.run();
                err = libisssr.destroy();
                err = libisssr.setMachineCode(mMechineCode);
                Log.i(TAG, "err1111: "+err);
                err = libisssr.activate(strPath);
                Log.i(TAG, "ActivateAsr:libisssr.activate: "+err);
                err = libisssr.createEx(0, strPath, mSRListener);
                Log.i(TAG, "err3333: "+err);
                err = libisssr.setParam(libisssr.ISS_SR_PARAM_TRACE_LEVEL,"2");
                Log.i(TAG, "err2222: "+err);
                err = libisssr.setParam(libisssr.ISS_SR_PARAM_RESPONSE_TIMEOUT, "10000");
                Log.i(TAG, "err4444: "+err);
            }
        }.start();

    }

    //初始化本地模型
    private void initTensorFlow(){
        InputStream intentPath = getClass().getResourceAsStream("/assets/intent_model.pb");
        InputStream slotPath = getClass().getResourceAsStream("/assets/slot_model.pb");
        tensorFlowInferenceIntent = new TensorFlowInferenceInterface(intentPath);
        tensorFlowInferenceSlot = new TensorFlowInferenceInterface(slotPath);

        //获取词典
        Map<String, ?> intent2indexMap = DictionaryGetUtils.getIntent2indexMap(this);
        Map<String, ?> index2intentMap = DictionaryGetUtils.getIndex2intentMap(this);
        Map<String, ?> slot2indexMap = DictionaryGetUtils.getSlot2indexMap(this);
        Map<String, ?> index2slotMap = DictionaryGetUtils.getIndex2slotMap(this);
        Map<String, ?> char2indexMap = DictionaryGetUtils.getChar2indexMap(this);
        Map<String, ?> index2charMap = DictionaryGetUtils.getIndex2charMap(this);
        dictionaryBean = new DictionaryBean();
        Map<String,Map<String,?>> map = new HashMap<>();
        map.put("intent2indexMap",intent2indexMap);
        map.put("index2intentMap",index2intentMap);
        map.put("slot2indexMap",slot2indexMap);
        map.put("index2slotMap",index2slotMap);
        map.put("char2indexMap",char2indexMap);
        map.put("index2charMap",index2charMap);
        dictionaryBean.setDictionary(map);
    }

    //广播接收
    private class MBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            final String asrResponse = intent.getStringExtra(ASR_RESPONSE);
            int asrState = intent.getIntExtra(ASR_STATE,ASR_STATE_DEFAULT);
            boolean loginSuccess = intent.getBooleanExtra(LOGIN_STATE,false);
            int wakeUp = intent.getIntExtra(WAKE_UP_STATE,AWAIT_WAKE_UP);
            if (wakeUp==WAKE_UP){
                //开始识别
                startRecord();
                err = libisssr.start("all", ISS_SR_MODE_LOCAL_REC, null);
                //弹出悬浮窗
                MyApplication.getInstance().getmFloatingView().show();
                //悬浮窗计时归零
                fvHandler.removeCallbacks(fvTask);
                time = 0;
            }else {
                //Log.i(TAG, "onReceive: 没有收到唤醒广播，不开启识别");
            }

            if (loginSuccess){    //登录成功，开启socket或轮询取消息
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        if (useSocket){
//                            //initSocket();
//                        }else {
//                            handler.post(task);  //开启轮询
//                        }
//                    }
//                }.start();
            }
            if (null!=asrResponse&&!asrResponse.equals("")){
                if (MyApplication.getInstance().getSceneSpeechs().contains(asrResponse)){
                    L.i("测试speechs：语句中包含了拦截词"+asrResponse);
                }else {
                    //messageCreatPresenter.messageCreat(asrResponse,context);  //新增消息
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            //拦截本地字符串匹配的语句
                            if (asrResponse.contains("连续对话")&&asrResponse.contains("开")){
                                sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"已为您开启连续对话");
                            }else if (asrResponse.contains("连续对话")&&asrResponse.contains("关")){
                                sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"已为您关闭连续对话");
                            }else {
                                //本地模型预测
                                //拼音加中文
                                //String pinyinAndChinese = PinyinDemo.ToPinyin(asrResponse)+asrResponse;
                                //L.i("ASR拼音+中文:"+pinyinAndChinese);
                                //纯拼音
                                String piniyn = PinyinDemo.ToPinyin(asrResponse).trim();
                                L.i("ASR拼音:"+piniyn);
                                ResultBean resultBean = ResultDealUtils.modelForecast(getApplicationContext(), piniyn, "u2a_speech", "123", context_Pe, query_Pe, tensorFlowInferenceIntent, tensorFlowInferenceSlot,dictionaryBean);
                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                L.i("本地模型预测结果speech:"+ gson.toJson(resultBean));
                                sendBroadcast(null,0,LOG_LOCAL,gson.toJson(resultBean));
                            }

                        }
                    });
                }



            }
            switch (asrState){
                case ASR_STATE_RESPONSE_TIMEOUT:
                    //MyApplication.getInstance().getmFloatingView().hide();
                    break;
                case ASR_STATE_SPEECH_START:

                    break;
                case ASR_STATE_SPEECH_TIMEOUT:

                    break;
                case ASR_STATE_SPEECH_END:

                    break;
                case ASR_STATE_ERROR:
                    MyApplication.getInstance().getmFloatingView().hide();
                    break;
                case ASR_STATE_DEFAULT:
                    break;
            }
        }
    }


    //发送广播
    private void sendBroadcast(String key1,int value1,String key2,String value2){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key1,value1);
        intent.putExtra(key2,value2);
        sendBroadcast(intent);
    }

    private void sendBroadcast(String key1,String value1){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key1,value1);
        sendBroadcast(intent);
    }
    private void sendBroadcast(String key1,int value1){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key1,value1);
        sendBroadcast(intent);
    }





    @Override
    public boolean appendAudioData(byte[] buff) {
        //Log.i(TAG, "appendAudioData: ASR");
        return libisssr.appendAudioData(buff, buff.length) == ISSErrors.ISS_SUCCESS;
    }


    private ISRListener mSRListener = new ISRListener() {
        @Override
        public void onSRMsgProc_(long uMsg, long wParam, String lParam) {
            handleSRMessage(uMsg, wParam, lParam);
        }
    };


    private void startRecord(){
        Log.i(TAG, "startRecord: 开始录音");
        SoundPlayUtils.getInstance(getApplication()).playSound(SoundPlayUtils.WAKE_UP_SOUND);
        SSRecorder.instance().start(SSRecorder.RECORDTYPE.RECORD_SR);
        sendGreenLight();
    }
    private void stopRecord(){
        Log.i(TAG, "startRecord: 结束录音");
        SSRecorder.instance().stop(SSRecorder.RECORDTYPE.RECORD_SR);
        sendRedLight();
    }

    private boolean handleSRMessage(long msg, long wParam, String lParam){
        boolean rtn = false;
        //Log.i(TAG, "handleSRMessage:msgmsgmsgmsg: "+(int) msg+"     wParam:"+wParam+"   lParam:"+formatJson(lParam));
        switch ((int) msg) {
            case libisssr.ISS_SR_MSG_InitStatus: {
                if (wParam == 0){
                    Log.i(TAG, "handleSRMessage: 识别初始化成功");
                    SSRecorder.instance().registRecordType(SSRecorder.RECORDTYPE.RECORD_SR, this);
                }else {
                    Log.i(TAG, "handleSRMessage: 识别初始化失败, 错误码:"+ wParam);
                }
            }
            break;
            case libisssr.ISS_SR_MSG_UpLoadDictToLocalStatus: {
                Log.i(TAG, "handleSRMessage: 上传词典到本地完成, 错误码 :" + wParam);
            }
            break;
            case libisssr.ISS_SR_MSG_UpLoadDictToCloudStatus: {
                Log.i(TAG, "handleSRMessage: 上传词典到云端完成, 错误码 :" + wParam);
            }
            break;
            case libisssr.ISS_SR_MSG_VolumeLevel: {
                currentVolume = (int)wParam;
            }
            break;
            case libisssr.ISS_SR_MSG_ResponseTimeout: {
                Log.i(TAG, "handleSRMessage: 识别超时");
                sendRedLight();
                handlerVolume.removeCallbacks(taskVolume);
                sendBroadcast(ASR_STATE,ASR_STATE_RESPONSE_TIMEOUT);
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechStart: {
                Log.i(TAG, "handleSRMessage: 检测到语音开始");
                sendGreenLight();
                handlerVolume.post(taskVolume);
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_START);
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechTimeOut: {
                Log.i(TAG, "handleSRMessage: 说话超时");
                sendRedLight();
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_TIMEOUT);
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechEnd: {
                Log.i(TAG, "handleSRMessage: 检测到语音结束");
                sendRedLight();
                handlerVolume.removeCallbacks(taskVolume);
                stopRecord();
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_END);
            }
            break;
            case libisssr.ISS_SR_MSG_Error: {
                Log.i(TAG, "handleSRMessage: 识别出错");
                sendRedLight();
                handlerVolume.removeCallbacks(taskVolume);
                sendBroadcast(ASR_STATE,ASR_STATE_ERROR);
            }
            break;
            case libisssr.ISS_SR_MSG_Result: {
                //Log.i(TAG, "handleSRMessage: 识别结果:\n"+formatJson(lParam));
                try {
                    JSONObject object = new JSONObject(lParam);
                    String text = object.getString("text");
                    String normal_text = object.getString("normal_text");
                    //Log.i(TAG,"text:"+text);
                    Log.i(TAG,"normal_text:"+normal_text);
                    sendBroadcast(ASR_RESPONSE,normal_text);
                    //TODO 重置FloatingView隐藏时间
                    fvHandler.removeCallbacks(fvTask);
                    fvHandler.post(fvTask);
                    time = 0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case libisssr.ISS_SR_MSG_LoadBigSrResStatus: {

            }
            break;
            case libisssr.ISS_SR_MSG_ErrorDecodingAudio: {

            }
            break;
            case libisssr.ISS_SR_MSG_PreResult: {
                Log.i(TAG, "handleSRMessage: 预处理结果:\n"+lParam);
            }
            break;
            case libisssr.ISS_SR_MSG_CloudInitStatus: {
                L.i("混合识别模式下，云端初始化状态");
            }
            break;
            case libisssr.ISS_SR_MSG_RealTimeResult: {

            }
            break;
            case libisssr.ISS_SR_MSG_WaitingForCloudResult: {
                L.i("混合识别模式下已出本地结果同时等待云端结果");
            }
            break;
            case libisssr.ISS_SR_MSG_Res_Update_Start: {

            }
            break;
            case libisssr.ISS_SR_MSG_Res_Update_End: {

            }
            break;
            case libisssr.ISS_SR_MSG_WaitingForLocalResult: {
                L.i("混合识别模式下已出云端结果正在等待本地结果");
            }
            break;
            case libisssr.ISS_SR_MSG_STKS_Result: {

            }
            break;
            case libisssr.ISS_SR_MSG_ONESHOT_MVWResult: {

            }
            break;

            default: {
                Log.i(TAG, "message = " + msg + "\t wParam = " + wParam + "\t lParam = " + lParam);
            }
            break;
        }
        return rtn;
    }

    private void sendGreenLight(){
        UDPSocketCommand.getInstance().sendUdpAndWaitRes(byteMerger(byteMerger(FRAME_COMMAND_0,FRAME_COMMAND_1), FlightControlContants.LISTENNING));
    }

    private void sendRedLight(){
        UDPSocketCommand.getInstance().sendUdpAndWaitRes(byteMerger(byteMerger(FRAME_COMMAND_0,FRAME_COMMAND_1), FlightControlContants.DISPOSING));
    }
}

