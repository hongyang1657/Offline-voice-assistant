package fitme.ai.zotyeautoassistant.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iflytek.speech.ISSErrors;
import com.iflytek.speech.libisssr;
import com.iflytek.speech.sr.ISRListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import fitme.ai.zotyeautoassistant.MyApplication;
import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.bean.DictionaryBean;
import fitme.ai.zotyeautoassistant.bean.MessageBody;
import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.bean.Messages;
import fitme.ai.zotyeautoassistant.bean.ResultBean;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.presenter.GetDeviceConfigPresenter;
import fitme.ai.zotyeautoassistant.presenter.MessageArrivedPresenter;
import fitme.ai.zotyeautoassistant.presenter.MessageCreatPresenter;
import fitme.ai.zotyeautoassistant.presenter.MessageGetPresenter;
import fitme.ai.zotyeautoassistant.presenter.SmartSceneControlPresenter;
import fitme.ai.zotyeautoassistant.utils.ChatItemTypeConsts;
import fitme.ai.zotyeautoassistant.utils.DictionaryGetUtils;
import fitme.ai.zotyeautoassistant.utils.IAppendAudio;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.PinyinDemo;
import fitme.ai.zotyeautoassistant.utils.ResultDealUtils;
import fitme.ai.zotyeautoassistant.utils.SSRecorder;
import fitme.ai.zotyeautoassistant.utils.StringUtils;
import fitme.ai.zotyeautoassistant.utils.VolumeUtil;
import fitme.ai.zotyeautoassistant.view.impl.IMessageManageService;
import okhttp3.ResponseBody;

import static com.iflytek.speech.libisssr.ISS_SR_MODE_LOCAL_REC;
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
import static fitme.ai.zotyeautoassistant.utils.Constants.LOG;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOGIN_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOG_LOCAL;
import static fitme.ai.zotyeautoassistant.utils.Constants.TAG;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_CONTROL;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_START;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_TEXT;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP_STATE;

public class NLPMessageService extends Service implements IMessageManageService,IAppendAudio {

    private boolean useSocket = false;   //是否使用socket获取消息
    //轮询获取消息
    private MessageGetPresenter messageGetPresenter;
    //消息到达 Socket不需要调用此接口
    private MessageArrivedPresenter messageArrivedPresenter;
    //创建新消息
    private MessageCreatPresenter messageCreatPresenter;
    private SmartSceneControlPresenter smartSceneControlPresenter;
    //获取设备配置信息
    private GetDeviceConfigPresenter getDeviceConfigPresenter;
    private MBroadcastReceiver mBroadcastReceiver;
    private ExecutorService executorService;

    //当前进行到的网络请求消息的次数,每次发送消息重置
    private int nowTime;
    //定时循环执行任务
    private Handler handler = new Handler();
    //定时处理runnable
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (nowTime <= 3) {
                getMessage();
                nowTime += 1;
                handler.postDelayed(this, 300);
            } else if (nowTime > 3 && nowTime <= 6) {
                getMessage();
                nowTime += 1;
                handler.postDelayed(this, 1000);
            } else if (nowTime > 6 && nowTime <= 10) {
                getMessage();
                nowTime += 1;
                handler.postDelayed(this, 3000);
            }else{
                getMessage();
                nowTime += 1;
                handler.postDelayed(this, 10000);

            }
        }
    };

    private Intent intentMusic = null;
    private String url = "";
    private String playerType;
    private String playerSecond;

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
        socketHandler.removeCallbacksAndMessages(null);
        if (intentMusic!=null){
            stopService(intentMusic);
        }
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
                //Log.i(TAG, "err1111: "+err);
                err = libisssr.activate(strPath);
                //Log.i(TAG, "ActivateAsr:libisssr.activate: "+err);
                err = libisssr.createEx(0, strPath, mSRListener);
                //Log.i(TAG, "err3333: "+err);
                err = libisssr.setParam(libisssr.ISS_SR_PARAM_TRACE_LEVEL,"2");
                //Log.i(TAG, "err2222: "+err);
                err = libisssr.setParam(libisssr.ISS_SR_PARAM_RESPONSE_TIMEOUT, "20000");
                //Log.i(TAG, "err4444: "+err);
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

    private void initPresenter(){
        messageGetPresenter = new MessageGetPresenter(this);
        messageArrivedPresenter = new MessageArrivedPresenter(this);
        messageCreatPresenter = new MessageCreatPresenter(this);
        smartSceneControlPresenter = new SmartSceneControlPresenter(this);
        getDeviceConfigPresenter = new GetDeviceConfigPresenter(this);
        socketHandler = new SocketHandler(this);
        intentMusic = new Intent(this,MusicPlayerService.class);
    }

    //socket推送消息处理
    private Handler socketHandler;
    private class SocketHandler extends Handler{
        private WeakReference<NLPMessageService> WeakReference;

        public SocketHandler(NLPMessageService nlpMessageService){
            WeakReference = new WeakReference<NLPMessageService>(nlpMessageService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NLPMessageService service = WeakReference.get();
            if (service!=null){
                switch (msg.what){
                    case 0:
                        MessageGet messageGet = (MessageGet) msg.obj;
                        messageResManager(messageGet);
                        break;
                }
            }

        }
    }

    //用户新增消息
    private void getMessage(){
        messageGetPresenter.messageGet();
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
                    smartSceneControlPresenter.sceneActivate(asrResponse,context);
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
                                Gson gson = new GsonBuilder()
                                        .disableHtmlEscaping()
                                        .create();
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

    /**
     *   消息处理
     */
    private String messageType;
    private void messageResManager(MessageGet messageGet){
        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()//打开之后log打印会出现空格
                .disableHtmlEscaping()
                .create();
        sendBroadcast(null,0,LOG,gson.toJson(messageGet));
        // L.i("messageGet:"+new Gson().toJson(messageGet));
        if (messageGet.getMessages() == null) {
            return;
        }
        if (messageGet.getMessages().length != 0) {
            for (Messages messages : messageGet.getMessages()) {
                L.i("消息id："+messages.getMessage_id());
                if (!useSocket){
                    messageArrivedPresenter.messageArrived(messages.getMessage_id());  //TODO Socket 不需要调用此接口
                }
            }
        }
        for (Messages message : messageGet.getMessages()) {
            messageType = message.getMessage_type();
            //当intent="release_turn"时，音箱端表示多轮对话，开启唤醒
            MessageBody messageBody = message.getMessage_body();
            if (messageBody != null && messageBody.getIntent() != null && "release_turn".equals(messageBody.getIntent())) {
                L.i("收到release_turn开启唤醒");
                //TODO 收到release_turn开启唤醒

                continue;
            }else {
                //L.i("未收到release_turn不开启开启唤醒");
            }
            L.i("MyApplication.getInstance().getMsgId():"+ MyApplication.getInstance().getMsgId()+" -----message.getMessage_id():"+message.getMessage_id());
            if (!MyApplication.getInstance().getMsgId().contains(message.getMessage_id())) {   //不处理重复messageId的消息
                MyApplication.getInstance().getMsgId().add(message.getMessage_id());
                if (StringUtils.isEmpty(message.getFrom_message_id())) {
                    continue;
                }
                if (messageType.contains("speech")&&!ChatItemTypeConsts.SPEECH_TYPE_AUTO_U2A_SPEECH.equals(messageType)){
                    //需要TTS播报的内容,发送广播
                    String speech = messageBody.getSpeech();

                    if (isMessageIdLegal(message.getMessage_id())){
                        if (speech!=null&&!"".equals(speech)){
                            L.i("播报的speech:"+speech);
                            playingmusic(MusicPlayerService.PAUSE_MUSIC,url,0);   //暂停播放
                            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,speech);
                        }
                    }

                    continue;
                }else {
                    L.i("不包含speech，结束悬浮框");
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(5000);
                                //MyApplication.getInstance().getmFloatingView().hide();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }
                if (ChatItemTypeConsts.SPEECH_TYPE_API_CALL_ACTION.equals(messageType)){

                    String intent = message.getMessage_body().getIntent();
                    L.i("音频Intent:"+intent);
                    if ("配置有变更".equals(intent)){
                        //TODO 请求获取新的配置信息
                        getDeviceConfigPresenter.getDeviceConfig();
                    } else if ("暂停播放".equals(intent)){
                        //TODO 暂停播放
                        playingmusic(MusicPlayerService.STOP_MUSIC,url,0);
                    } else if ("播放<speech><url><播放类别><循环秒数><缓存秒数><位置>".equals(intent)){     //播放
                        url = messageBody.getSlots().getUrl();
                        String slotSpeech = messageBody.getSlots().getSpeech();
                        playerType = messageBody.getSlots().get播放类别();
                        playerSecond = messageBody.getSlots().get循环秒数();
                        String playerPosition = messageBody.getSlots().get位置();
                        if ("内容".equals(playerType)){
                            playingmusic(MusicPlayerService.PLAT_MUSIC,url,Integer.valueOf(playerPosition));   //播放音乐
                        }else if ("通知".equals(playerType)){
                            //TODO 传一个播放时间，只播放这个时间
                            playingmusic(MusicPlayerService.PLAY_INFORM,url,Integer.valueOf(playerPosition));
                            L.i("收到通知，循环秒数："+playerSecond);
                        }


                    }else if ("调大音量<调节量>".equals(intent)){
                        VolumeUtil.getInstance(this).setVolumePlus(Integer.valueOf(message.getMessage_body().getSlots().getVolume()));
                    }else if ("调小音量<调节量>".equals(intent)){
                        VolumeUtil.getInstance(this).setVolumePlus(-(Integer.valueOf(message.getMessage_body().getSlots().getVolume())));
                    }else if ("设置音量<音量>".equals(intent)){
                        VolumeUtil.getInstance(this).setVolumeValue(Integer.valueOf(message.getMessage_body().getSlots().getVolume()));
                    }

                } else if (ChatItemTypeConsts.SPEECH_TYPE_URL_IMG_TITLE_H1.equals(messageType)){
                }else if(ChatItemTypeConsts.SPEECH_TYPE_STANDARD.equals(messageType)){
                }else if (ChatItemTypeConsts.SPEECH_TYPE_RELAY_COMMAND.equals(messageType)) {

                }else if (ChatItemTypeConsts.SPEECH_TYPE_AUTO_U2A_SPEECH.equals(messageType)){
                    L.i("messageType：SPEECH_TYPE_AUTO_U2A_SPEECH："+new Gson().toJson(message.getMessage_body()));
                    //TODO 收到speech后发出去
                    //messageCreatPresenter.messageCreat(message.getMessage_body().getSpeech(),this);
                }
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

    //判断messageId是否过期
    private boolean isMessageIdLegal(String messageId){
        if (MyApplication.getInstance().getMessageId().contains(messageId)){      //返回的数据包含此message（表示用户对话得到回应）
            if (messageId.equals(MyApplication.getInstance().getMessageId().get(0))){   //messageId在首位
                return true;
            }else {
                return false;
            }
        }else {   //表示收到了任务或主动推送的消息
            return true;
        }
    }

    @Override
    public void getMessageResp(MessageGet messageGet) {
        L.i("消息中心http:"+new Gson().toJson(messageGet));
        messageResManager(messageGet);
    }

    @Override
    public void getMessageArrivedResp(Status status) {
        if ("success".equals(status.getStatus())){
            L.i("消息到达返回成功");
        }
    }

    @Override
    public void getMessageCreatResp(Status status) {
        L.i("新增数据回调"+new Gson().toJson(status));
        //请求成功，将当前请求的次数设置为0
//        if (!useSocket){
//            nowTime = 0;
//            handler.removeCallbacks(task);
//            handler.post(task);
//        }
    }

    @Override
    public void getSceneActivate(ResponseBody responseBody) {
        try {
            L.i("智能场景控制："+responseBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDeviceConfig(DeviceConfigBean deviceConfigBean) {
        L.i("获取指定交互设备的配置信息:"+new Gson().toJson(deviceConfigBean));
        int speechLength = deviceConfigBean.getDevice_config()[0].getUser_config().getSpeechs().length;
        for (int i=0;i<speechLength;i++){
            L.i("拦截词："+deviceConfigBean.getDevice_config()[0].getUser_config().getSpeechs()[i].getSpeech());
            MyApplication.getInstance().getSceneSpeechs().add(deviceConfigBean.getDevice_config()[0].getUser_config().getSpeechs()[i].getSpeech());
        }
    }

    /**
     * socket 连接与消息接收
     */
    /*private void initSocket(){
        L.i("初始化socket");
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        ConnectMessage connectMessage = new ConnectMessage(userId, Mac.getMac(), token);
        DefaultClientConnector connector = new DefaultClientConnector(connectMessage, new MsgHandleService() {
            @Override
            public boolean connectRespHandle(Channel channel, String str) {
                L.i("消息中心socket，connectRespHandle："+str);
                sendBroadcast(null,0,LOG,"socket connectRespHandle"+str);
                return true;
            }

            @Override
            public boolean msgDeliveHandle(Channel channel, String commonMessage) {
                L.i("消息中心socket，msgDeliveHandle："+commonMessage);
                //sendBroadcast(null,0,LOG,"socket msgDeliveHandle"+new Gson().toJson(commonMessage));
                Gson gson = new Gson();
                Messages messages = gson.fromJson(commonMessage,Messages.class);
                MessageGet messageGet = new MessageGet();
                messageGet.setStatus("success");
                Messages[] messages1 = new Messages[]{messages};
                messageGet.setMessages(messages1);

                Message msg = new Message();
                msg.what = 0;
                msg.obj = messageGet;
                socketHandler.sendMessage(msg);

                return true;
            }
        });
        L.i("SOCKET_NOTIFICATION_PORT:"+ ApiManager.SOCKET_NOTIFICATION_PORT+" ----SOCKET_NOTIFICATION_IP:"+ApiManager.SOCKET_NOTIFICATION_IP);
        connector.connect(ApiManager.SOCKET_NOTIFICATION_PORT,ApiManager.SOCKET_NOTIFICATION_IP);
    }*/

    private void playingmusic(int type,String songUrl,int position) {
        //启动服务，播放音乐
//        intentMusic.putExtra("type",type);
//        intentMusic.putExtra("songUrl",songUrl);
//        intentMusic.putExtra("position",position);
//
//        if (type==MusicPlayerService.PLAT_MUSIC){
//            intentMusic.putExtra("isTTSing",false);
//            intentMusic.putExtra("playerType",playerType);
//        }else if (type==MusicPlayerService.PLAY_INFORM){
//            intentMusic.putExtra("playerSecond",playerSecond);
//        }
//        startService(intentMusic);
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
                handlerVolume.removeCallbacks(taskVolume);
                sendBroadcast(ASR_STATE,ASR_STATE_RESPONSE_TIMEOUT);
                playingmusic(MusicPlayerService.RECOVER_MUSIC_VOLUME,"",0);     //恢复音乐音量
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechStart: {
                Log.i(TAG, "handleSRMessage: 检测到语音开始");
                handlerVolume.post(taskVolume);
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_START);
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechTimeOut: {
                Log.i(TAG, "handleSRMessage: 说话超时");
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_TIMEOUT);
                playingmusic(MusicPlayerService.RECOVER_MUSIC_VOLUME,"",0);     //恢复音乐音量
            }
            break;
            case libisssr.ISS_SR_MSG_SpeechEnd: {
                Log.i(TAG, "handleSRMessage: 检测到语音结束");
                handlerVolume.removeCallbacks(taskVolume);
                stopRecord();
                sendBroadcast(ASR_STATE,ASR_STATE_SPEECH_END);
            }
            break;
            case libisssr.ISS_SR_MSG_Error: {
                Log.i(TAG, "handleSRMessage: 识别出错");
                handlerVolume.removeCallbacks(taskVolume);
                sendBroadcast(ASR_STATE,ASR_STATE_ERROR);
                playingmusic(MusicPlayerService.RECOVER_MUSIC_VOLUME,"",0);     //恢复音乐音量
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
}

