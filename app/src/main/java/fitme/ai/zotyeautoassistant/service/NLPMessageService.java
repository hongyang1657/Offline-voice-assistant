package fitme.ai.zotyeautoassistant.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ai.fitme.notification.client.DefaultClientConnector;
import ai.fitme.notification.client.service.MsgHandleService;
import ai.fitme.notification.common.message.ConnectMessage;
import fitme.ai.zotyeautoassistant.MyApplication;
import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.bean.DictionaryBean;
import fitme.ai.zotyeautoassistant.bean.MessageBody;
import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.bean.Messages;
import fitme.ai.zotyeautoassistant.bean.ResultBean;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.presenter.MessageArrivedPresenter;
import fitme.ai.zotyeautoassistant.presenter.MessageCreatPresenter;
import fitme.ai.zotyeautoassistant.presenter.MessageGetPresenter;
import fitme.ai.zotyeautoassistant.utils.ChatItemTypeConsts;
import fitme.ai.zotyeautoassistant.utils.DictionaryGetUtils;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.Mac;
import fitme.ai.zotyeautoassistant.utils.ResultDealUtils;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.StringUtils;
import fitme.ai.zotyeautoassistant.utils.UnicodeUtil;
import fitme.ai.zotyeautoassistant.utils.VolumeUtil;
import fitme.ai.zotyeautoassistant.view.impl.IMessageManageService;
import io.netty.channel.Channel;

import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_RESPONSE;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_DEFAULT;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_ERROR;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_RESPONSE_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_SPEECH_END;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_SPEECH_START;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_STATE_SPEECH_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Contansts.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Contansts.LOG;
import static fitme.ai.zotyeautoassistant.utils.Contansts.LOGIN_STATE;
import static fitme.ai.zotyeautoassistant.utils.Contansts.LOG_LOCAL;
import static fitme.ai.zotyeautoassistant.utils.Contansts.TTS_CONTROL;
import static fitme.ai.zotyeautoassistant.utils.Contansts.TTS_START;
import static fitme.ai.zotyeautoassistant.utils.Contansts.TTS_TEXT;
import static java.lang.Thread.sleep;

public class NLPMessageService extends Service implements IMessageManageService{

    private boolean useSocket = false;   //是否使用socket获取消息
    //轮询获取消息
    private MessageGetPresenter messageGetPresenter;
    //消息到达 Socket不需要调用此接口
    private MessageArrivedPresenter messageArrivedPresenter;
    //创建新消息
    private MessageCreatPresenter messageCreatPresenter;
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPresenter();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                initTensorFlow();
            }
        });
        L.i("创建NLPMessageService");
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

        socketHandler = new SocketHandler(this);
        //注册广播
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        registerReceiver(mBroadcastReceiver,intentFilter);

        intentMusic = new Intent(this,MusicPlayerService.class);
        executorService = Executors.newCachedThreadPool(); //线程池
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
            if (loginSuccess){    //登录成功，开启socket或轮询取消息
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if (useSocket){
                            initSocket();
                        }else {
                            handler.post(task);  //开启轮询
                        }
                    }
                }.start();
            }
            if (null!=asrResponse&&!asrResponse.equals("")){
                messageCreatPresenter.messageCreat(asrResponse,context);  //新增消息

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        //本地模型预测
                        ResultBean resultBean = ResultDealUtils.modelForecast(getApplicationContext(), asrResponse, "u2a_speech", "123", context_Pe, query_Pe, tensorFlowInferenceIntent, tensorFlowInferenceSlot,dictionaryBean);
                        Gson gson = new GsonBuilder().setPrettyPrinting()//打开之后log打印会出现空格
                                .disableHtmlEscaping()
                                .create();
                        L.i("本地模型预测结果speech:"+ gson.toJson(resultBean));
                        sendBroadcast(null,0,LOG_LOCAL,gson.toJson(resultBean));


                    }
                });

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
                continue;
            }else {
                //L.i("未收到release_turn不开启开启唤醒");
            }
            L.i("MyApplication.getInstance().getMsgId():"+ MyApplication.getInstance().getMsgId()+" -----message.getMessage_id():"+message.getMessage_id());
            if (!MyApplication.getInstance().getMsgId().contains(message.getMessage_id())) {
                MyApplication.getInstance().getMsgId().add(message.getMessage_id());
                if (StringUtils.isEmpty(message.getFrom_message_id())) {
                    continue;
                }
                if (messageType.contains("speech")&&!ChatItemTypeConsts.SPEECH_TYPE_AUTO_U2A_SPEECH.equals(messageType)){
                    //需要TTS播报的内容,发送广播
                    String speech = messageBody.getSpeech();
                    sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,speech);

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
        if (!useSocket){
            nowTime = 0;
            handler.removeCallbacks(task);
            handler.post(task);
        }
    }

    /**
     * socket 连接与消息接收
     */
    private void initSocket(){
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
    }

    private void playingmusic(int type,String songUrl,int position) {
        //启动服务，播放音乐
        intentMusic.putExtra("type",type);
        intentMusic.putExtra("songUrl",songUrl);
        intentMusic.putExtra("position",position);

        if (type==MusicPlayerService.PLAT_MUSIC){
            intentMusic.putExtra("isTTSing",false);
            intentMusic.putExtra("playerType",playerType);
        }else if (type==MusicPlayerService.PLAY_INFORM){
            intentMusic.putExtra("playerSecond",playerSecond);
        }
        startService(intentMusic);
    }

}

