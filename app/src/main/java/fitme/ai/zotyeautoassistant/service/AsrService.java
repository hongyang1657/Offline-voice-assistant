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

import com.iflytek.speech.ISSErrors;
import com.iflytek.speech.libisssr;
import com.iflytek.speech.sr.ISRListener;

import org.json.JSONException;
import org.json.JSONObject;

import fitme.ai.zotyeautoassistant.MyApplication;
import fitme.ai.zotyeautoassistant.utils.IAppendAudio;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.SSRecorder;

import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_RESPONSE;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_ERROR;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_RESPONSE_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_SPEECH_END;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_SPEECH_START;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_SPEECH_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_VOLUME;
import static fitme.ai.zotyeautoassistant.utils.Contansts.AWAIT_WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Contansts.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Contansts.TAG;
import static fitme.ai.zotyeautoassistant.utils.Contansts.WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Contansts.WAKE_UP_STATE;

public class AsrService extends Service implements IAppendAudio {

    int err = ISSErrors.ISS_SUCCESS;
    private MBroadcastReceiver mBroadcastReceiver;
    private int currentVolume = 0;
    private boolean waveReturnZero = true;
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (waveReturnZero){
                sendBroadcast(ASR_VOLUME,currentVolume);
                waveReturnZero = false;
            }else {
                sendBroadcast(ASR_VOLUME,0);
                waveReturnZero = true;
            }
            handler.postDelayed(this,200);
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
    private Intent intentMusic;

    @Override
    public void onCreate() {
        super.onCreate();
        L.i("创建AsrService");
        initAsr();
        //注册广播
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        registerReceiver(mBroadcastReceiver,intentFilter);
        intentMusic = new Intent(this,MusicPlayerService.class);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        err = libisssr.stop();
        err = libisssr.destroy();
        unregisterReceiver(mBroadcastReceiver);
        L.i("销毁AsrService");
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
                err = libisssr.setParam(libisssr.ISS_SR_PARAM_RESPONSE_TIMEOUT, "20000");
                Log.i(TAG, "err4444: "+err);
            }
        }.start();

    }

    //广播接收
    private class MBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int wakeUp = intent.getIntExtra(WAKE_UP_STATE,AWAIT_WAKE_UP);
            if (wakeUp==WAKE_UP){
                //开始识别
                startRecord();
                err = libisssr.start("all", 2, null);
                //弹出悬浮窗
                MyApplication.getInstance().getmFloatingView().show();
                //悬浮窗计时归零
                fvHandler.removeCallbacks(fvTask);
                time = 0;
            }else {
                //Log.i(TAG, "onReceive: 没有收到唤醒广播，不开启识别");
            }
        }
    }

    private ISRListener mSRListener = new ISRListener() {
        @Override
        public void onSRMsgProc_(long uMsg, long wParam, String lParam) {
            handleSRMessage(uMsg, wParam, lParam);
        }
    };


    private void startRecord(){
        Log.i(TAG, "startRecord: 开始录音");
        SSRecorder.instance().start(SSRecorder.RECORDTYPE.RECORD_SR);
    }
    private void stopRecord(){
        Log.i(TAG, "startRecord: 结束录音");
        SSRecorder.instance().stop(SSRecorder.RECORDTYPE.RECORD_SR);
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
                handler.removeCallbacks(task);
                sendBroadcast(ASR_STATE,ASR_STATE_RESPONSE_TIMEOUT);
                playingmusic(MusicPlayerService.RECOVER_MUSIC_VOLUME);     //恢复音乐音量
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechStart: {
                Log.i(TAG, "handleSRMessage: 检测到语音开始");
                handler.post(task);
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_START);
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechTimeOut: {
                Log.i(TAG, "handleSRMessage: 说话超时");
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_TIMEOUT);
                playingmusic(MusicPlayerService.RECOVER_MUSIC_VOLUME);     //恢复音乐音量
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechEnd: {
                Log.i(TAG, "handleSRMessage: 检测到语音结束");
                handler.removeCallbacks(task);
                stopRecord();
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_END);
            }
            break;
            case libisssr.ISS_SR_MSG_Error: {
                Log.i(TAG, "handleSRMessage: 识别出错");
                handler.removeCallbacks(task);
                sendBroadcast(ASR_STATE,ASR_STATE_ERROR);
                playingmusic(MusicPlayerService.RECOVER_MUSIC_VOLUME);     //恢复音乐音量
            }
            break;
            case libisssr.ISS_SR_MSG_Result: {
                //Log.i(TAG, "handleSRMessage: 识别结果:\n"+formatJson(lParam));
                try {
                    JSONObject object = new JSONObject(lParam);
                    String text = object.getString("text");
                    String normal_text = object.getString("normal_text");
                    Log.i(TAG,"text:"+text);
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


    @Override
    public boolean appendAudioData(byte[] buff) {
        //Log.i(TAG, "appendAudioData: ASR");
        return libisssr.appendAudioData(buff, buff.length) == ISSErrors.ISS_SUCCESS;
    }

    //发送广播
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

    private void playingmusic(int type) {
        //启动服务，播放音乐
        intentMusic.putExtra("type",type);
        startService(intentMusic);
    }
}
