package fitme.ai.zotyeautoassistant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fitme.ai.zotyeautoassistant.bean.AccountCreate;
import fitme.ai.zotyeautoassistant.bean.AccountGet;
import fitme.ai.zotyeautoassistant.bean.CheckToken;
import fitme.ai.zotyeautoassistant.bean.DeviceAllInfo;
import fitme.ai.zotyeautoassistant.bean.DevicesInfo;
import fitme.ai.zotyeautoassistant.bean.ResultBean;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.bean.TokenInfo;
import fitme.ai.zotyeautoassistant.presenter.DeviceBindPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceBindSuccessPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceConfigPutPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceInfoUploadPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceSearchPresenter;
import fitme.ai.zotyeautoassistant.presenter.GetUserIdByMobilePresenter;
import fitme.ai.zotyeautoassistant.presenter.TokenPresenter;
import fitme.ai.zotyeautoassistant.service.AsrService;
import fitme.ai.zotyeautoassistant.service.ActivateService;
import fitme.ai.zotyeautoassistant.service.NLPMessageService;
import fitme.ai.zotyeautoassistant.service.TtsService;
import fitme.ai.zotyeautoassistant.utils.Constants;
import fitme.ai.zotyeautoassistant.utils.FlightControlContants;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.Mac;
import fitme.ai.zotyeautoassistant.utils.PinyinDemo;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SoundPlayUtils;
import fitme.ai.zotyeautoassistant.utils.TimerUtil;
import fitme.ai.zotyeautoassistant.utils.UDPSocket;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;
import fitme.ai.zotyeautoassistant.view.impl.TimerEndListener;
import okhttp3.ResponseBody;

import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_RESPONSE;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_DEFAULT;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_ERROR;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_RESPONSE_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_SPEECH_END;
import static fitme.ai.zotyeautoassistant.utils.Constants.AWAIT_WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOG;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOGIN_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOG_LOCAL;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_CONTROL;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_PLAY_END;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_START;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_TEXT;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_UNKNOW;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP_STATE;
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_0;
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_1;

/**
 * 用于众泰车机的语音交互程序
 * attention ：1.使用4G信号，不用wifi
 */

public class MainActivity extends Activity implements ILoginFragmentView{

    private MBroadcastReceiver mBroadcastReceiver;
    private ExecutorService executorService;
    private Intent intentActivateService = null;
    private Intent intentAsrService = null;
    private Intent intentTtsService = null;
    private Intent intentMessageService = null;
    private ScrollView scrollView;
    private EditText etAccount,etPassword;
    private TextView tvLog,tvLogLocal;
    private Context mContext;
    //获取Token
    private TokenPresenter mTokenPresenter;
    //根据手机号获取user_id
    private GetUserIdByMobilePresenter mGetUserIdByMobilePresenter;
    //绑定用户
    private DeviceBindPresenter deviceBindPresenter;
    //绑定成功
    private DeviceBindSuccessPresenter deviceBindSuccessPresenter;
    //上传硬件信息
    private DeviceInfoUploadPresenter deviceInfoUploadPresenter;
    //交互设备查询
    private DeviceSearchPresenter deviceSearchPresenter;
    //上传音箱的配置信息
    private DeviceConfigPutPresenter deviceConfigPutPresenter;

    private boolean isLocalLog = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化
        initView();
        executorService = Executors.newCachedThreadPool(); //线程池
        findViewById(R.id.bt_show_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().getmFloatingView().show();
                sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"测试播放，今天天气很好");
            }
        });
        findViewById(R.id.bt_hide_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().getmFloatingView().hide();
            }
        });
        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        mGetUserIdByMobilePresenter.getUserIdByMobile(etAccount.getText().toString().trim());
                    }
                });
            }
        });
        findViewById(R.id.bt_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocalLog){
                    tvLogLocal.setVisibility(View.GONE);
                    tvLog.setVisibility(View.VISIBLE);
                    isLocalLog = false;
                }else {
                    tvLog.setVisibility(View.GONE);
                    tvLogLocal.setVisibility(View.VISIBLE);
                    isLocalLog = true;
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UDPSocket.getInstance().sendUdpAndWaitRes(FRAME_0);
            }
        });
        tvLog.setVisibility(View.GONE);
        tvLogLocal.setVisibility(View.VISIBLE);
        isLocalLog = true;
        //initPresenter();
        initActivateService();
        //initAsrService();
        initTtsService();
        initMessageService();
        //广播接收
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        registerReceiver(mBroadcastReceiver,intentFilter);
        mContext = this;

    }

    //发送广播
    private void sendBroadcast(String key1,int value1,String key2,String value2){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key1,value1);
        intent.putExtra(key2,value2);
        sendBroadcast(intent);
    }

    //发送广播
    private void sendBroadcast(String key,int value){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key,value);
        sendBroadcast(intent);
    }


    //初始化接口
    private void initPresenter(){

        mTokenPresenter = new TokenPresenter(this);
        mGetUserIdByMobilePresenter = new GetUserIdByMobilePresenter(this);
        deviceBindPresenter = new DeviceBindPresenter(this);
        deviceBindSuccessPresenter = new DeviceBindSuccessPresenter(this);
        deviceInfoUploadPresenter = new DeviceInfoUploadPresenter(this);
        deviceSearchPresenter = new DeviceSearchPresenter(this);
        deviceConfigPutPresenter = new DeviceConfigPutPresenter(this);


    }

    private void initView(){
        scrollView = findViewById(R.id.scroll_view);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        tvLog = findViewById(R.id.tv_log_cloud);
        tvLogLocal = findViewById(R.id.tv_log_local);
        SharedPreferencesUtils.getInstance().setIntKeyValue(SPConstants.SP_AYAH_LONGTITUDE,1202106130);
        SharedPreferencesUtils.getInstance().setIntKeyValue(SPConstants.SP_AYAH_LATITUDE,302644590);
        TimerUtil.getInstance().setOnTimerEndListener(new TimerEndListener() {
            @Override
            public void timeEnd() {
                L.i("倒计时结束");
            }
        });
    }

    private void initActivateService(){
        intentActivateService = new Intent(this, ActivateService.class);
        startService(intentActivateService);
    }

    private void initAsrService(){
        intentAsrService = new Intent(MainActivity.this, AsrService.class);
        startService(intentAsrService);
    }

    private void initTtsService(){
        intentTtsService = new Intent(MainActivity.this, TtsService.class);
        startService(intentTtsService);
    }

    private void initMessageService(){
        intentMessageService = new Intent(MainActivity.this, NLPMessageService.class);
        startService(intentMessageService);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intentActivateService !=null){
            stopService(intentActivateService);
        }
        if (intentAsrService!=null){
            stopService(intentAsrService);
        }
        if (intentTtsService!=null){
            stopService(intentTtsService);
        }
        if (intentMessageService!=null){
            stopService(intentMessageService);
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    private class MBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int wakeUp = intent.getIntExtra(WAKE_UP_STATE,AWAIT_WAKE_UP);
            String log = intent.getStringExtra(LOG);
            String log_local = intent.getStringExtra(LOG_LOCAL);
            String asrResponse = intent.getStringExtra(ASR_RESPONSE);
            int ttsState = intent.getIntExtra(TTS_STATE,TTS_UNKNOW);
            int asrState = intent.getIntExtra(ASR_STATE,ASR_STATE_DEFAULT);
            //云端log
//            if (null!=log&&!"{\"messages\":[],\"status\":\"success\"}".equals(log)){
//                if (tvLog.getTextSize() < 50000){
//                    tvLog.append(formatJson(log)+"\n");
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                        }
//                    });
//
//                }else {
//                    tvLog.setText(formatJson(log)+"\n");
//                }
//            }
            //本地log
            if (null!=log_local&&!"{\"messages\":[],\"status\":\"success\"}".equals(log_local)){
                if (tvLogLocal.getTextSize() < 50000){
                    tvLogLocal.append(formatJson(log_local)+"\n");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                }else {
                    tvLogLocal.setText(formatJson(log_local)+"\n");
                }
                Log.i("debug_result", "onReceive: "+log_local);
                Gson gson = new Gson();
                ResultBean resultBean = gson.fromJson(log_local, ResultBean.class);
                L.i("result---intent:"+resultBean.getIntent()+"预测分数intent_score："+resultBean.getIntent_score());
                switch (resultBean.getIntent()){
                    case Constants.START_FLYING_CONTROL_SYSTEM:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.ENTER_AIR_LINE:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.TAKE_OFF:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.INSTRUCT_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.TURN_LEFT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.TURN_RIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.STRAIGHT_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.CLIMB:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.LEVEL_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.DECLINE:
                        if (resultBean.getIntent_score()>0.97f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.AUTO_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.CHECK_ENGINE:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.RAISE_LANDING_GEAR:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.PUT_LANDING_GEAR:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.STOP_FLYING_CONTROL_SYSTEM:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                    case Constants.CLOSE_ENGINE_CONSOLE:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent());
                        }else {
                            defaultChat();
                        }
                        break;

                    case "确认执行指令":
                        //分数大于阈值
                        if (resultBean.getIntent_score()>0.7f){
                            //执行缓存1分钟的intent,如果缓存过期，则不作处理
                            if (isContinuousDialogue){          //开启连续对话情况下
                                if (TimerUtil.getInstance().getTimerState()&&null!=cacheIntent&&!"".equals(cacheIntent)){
                                    sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"为您执行"+cacheIntent+"操作");
                                    //TODO 执行命令
                                    executiveCommand(cacheIntent);
                                    L.i("发送udp");
                                    cacheIntent = "";  //执行后清空缓存
                                }else {         //没有缓存指令，继续静默唤醒
                                    //静默唤醒
                                    sendBroadcast(WAKE_UP_STATE,WAKE_UP);
                                }
                            }else {

                            }
                        }else {
                            defaultChat();
                        }


                        break;
                    case "关闭二次确认功能":
                        if (resultBean.getIntent_score()>0.9f){
                            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"已为您关闭语音确认功能");
                            executiveCommand(Constants.CLOSE_CONFIRMATION);
                            isOpenConfirmation = false;
                        }else {
                            defaultChat();
                        }
                        break;
                    case "开启二次确认功能":
                        if (resultBean.getIntent_score()>0.9f){
                            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"已为您打开语音确认功能");
                            executiveCommand(Constants.OPEN_CONFIRMATION);
                            isOpenConfirmation = true;
                        }else {
                            defaultChat();
                        }
                        break;
                    default:
                        defaultChat();
                        break;
                }

            }
            //唤醒
            if (wakeUp==WAKE_UP){
                //L.i("唤醒");
                Log.i("debug_result", "唤醒");
            }
            //asr
            if (null!=asrResponse&&!asrResponse.equals("")){
                tvLog.append("\nASR:"+asrResponse+"\n");
                tvLogLocal.append("\nASR:"+asrResponse+"\n");
                if (asrResponse.contains("连续对话")&&asrResponse.contains("开")){
                    isContinuousDialogue = true;
                }else if (asrResponse.contains("连续对话")&&asrResponse.contains("关")){
                    isContinuousDialogue = false;
                    TimerUtil.getInstance().stop();
                }
            }
            //tts状态
            if (ttsState==TTS_PLAY_END){
                if (isContinuousDialogue&&TimerUtil.getInstance().getTimerState()){
                    //静默唤醒
                    sendBroadcast(WAKE_UP_STATE,WAKE_UP);
                }
            }
            //asr状态
            if (asrState==ASR_STATE_ERROR||asrState==ASR_STATE_RESPONSE_TIMEOUT){  //识别出错,识别超时
                if (isContinuousDialogue&&TimerUtil.getInstance().getTimerState()){
                    //静默唤醒
                    sendBroadcast(WAKE_UP_STATE,WAKE_UP);
                }else if (!isContinuousDialogue){
                    //tts：问的问题貌似不太清楚，可以再问一遍
                    sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"问的问题貌似不太清楚，可以再问一遍");
                }
            }
        }
    }
    //是否开启连续对话功能
    private boolean isContinuousDialogue = true;
    //是否开启二次确认功能
    private boolean isOpenConfirmation = true;

    private void defaultChat(){
        //模拟闲聊，连续对话开启情况下不回复，直接进入下次唤醒
        if (isContinuousDialogue&&TimerUtil.getInstance().getTimerState()){
            //静默唤醒
            sendBroadcast(WAKE_UP_STATE,WAKE_UP);
        }else if(!isContinuousDialogue){
            //tts：问的问题貌似不太清楚，可以再问一遍
            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"问的问题貌似不太清楚，可以再说一遍");
        }
    }


    @Override
    public void showGetUserId(AccountCreate accountCreate) {
        if ("success".equals(accountCreate.getStatus())) {
            L.i("GetUserId成功");
            SharedPreferencesUtils.getInstance().setStringKeyValue(SPConstants.SP_AYAH_USERID,accountCreate.getUser_id());
            mTokenPresenter.token(accountCreate.getUser_id(), etPassword.getText().toString().trim());
        } else {
            L.i("登录失败，GetUserId失败,网络未连接");
        }
    }

    @Override
    public void showToken(String userId, TokenInfo tokenInfo) {
        if ("success".equals(tokenInfo.getStatus())) {
            //deviceSearchPresenter.deviceSearch(this);  //交互设备查询
            deviceBindPresenter.deviceBind(this); //绑定设备
        }
    }

    @Override
    public void getDeviceSearchRes(DevicesInfo devicesInfo) {
        L.i("交互设备查询："+new Gson().toJson(devicesInfo));
        if ("success".equals(devicesInfo.getStatus())){
            int deviceNum = devicesInfo.getDevices().size();
            L.i("上传deviceNum："+deviceNum);
            for (int i=0;i<deviceNum;i++){
                if (devicesInfo.getDevices().get(i).getDevice_id().equals(Mac.getMac())){
                    //该爱芽账号绑定了此音箱
                    deviceInfoUploadPresenter.deviceInfoUpload(this);
                    //上传音箱信息
                    String path = "device_config/device_all";
                    DeviceAllInfo deviceAllInfo = new DeviceAllInfo();
                    try {
                        deviceAllInfo.setSystem_version(""+mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0).versionCode);
                        deviceAllInfo.setWifi("FITME_MX");   //attention 用的是4G，没有wifi
                        deviceAllInfo.setSerial_number(Mac.getSerialNumber());
                        deviceAllInfo.setMac(Mac.getMac());
                        deviceAllInfo.setIp("192.168.1.1");  //attention 设置假ip
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    deviceConfigPutPresenter.deviceConfigPut(this,path,deviceAllInfo);
                }
            }

        }
    }

    @Override
    public void getDeviceInfoUpload(Status status) {
        L.i("上传硬件信息成功"+new Gson().toJson(status));
        if ("success".equals(status.getStatus())){
            //网络连接成功,登录成功,打开messageSocket
            sendBroadcast(LOGIN_STATE,true);
        }
    }

    @Override
    public void getDeviceBindRes(Status status) {
        L.i("绑定的结果:"+new Gson().toJson(status));
        //绑定成功才跳转
        if ("success".equals(status.getStatus())){
            deviceBindSuccessPresenter.deviceBindSuccess(this);
        }
    }

    @Override
    public void getDeviceBindSuccessRes(Status responseBody) {
        L.i("告诉用户中心绑定成功的结果："+responseBody.getStatus());
        //上传设备信息
        if ("success".equals(responseBody.getStatus())){
            deviceSearchPresenter.deviceSearch(this);
        }
    }



    @Override
    public void failedToConnect(Throwable e) {

    }



    @Override
    public void getDeviceConfigPut(ResponseBody responseBody) {

    }


    @Override
    public void showTokenFailed(Throwable e) {

    }



    @Override
    public void checkToken(String userId, CheckToken jsonObject) {

    }

    @Override
    public void showAccountGet(AccountGet accountGet) {

    }

    //格式化json
    public String formatJson(String jsonStr){
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    private void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    //发送广播
    private void sendBroadcast(String key1,boolean value1){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key1,value1);
        sendBroadcast(intent);
    }

    //执行控制命令
    private String cacheIntent;
    private void control(String intent){
        TimerUtil.getInstance().start();    //使用语音唤醒 开始进入倒计时
        if (isOpenConfirmation){      //需要二次确认
            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"请确认是否要执行"+intent+"操作");
            //TODO 记录用户的控制指令，等待用户确认
            cacheIntent = intent;
        }else {        //不需要二次确认，直接执行命令
            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"为您执行"+intent+"操作");
            //TODO 发送UDP指令
            L.i("发送udp");
            executiveCommand(intent);
        }
    }

    //根据intent执行命令
//    private void executiveCommand(final String intent){
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            UDPSocket.getInstance().sendUdpAndWaitRes(FlightControlContants.FRAME_0);
//                        }
//                    });
//
//                    Thread.sleep(50);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            UDPSocket.getInstance().sendUdpAndWaitRes(FlightControlContants.FRAME_1);
//                        }
//                    });
//
//                    Thread.sleep(50);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            UDPSocket.getInstance().sendUdpAndWaitRes(FlightControlContants.getCommandByIntent(intent));
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void executiveCommand(String intent){
        UDPSocket.getInstance().sendUdpAndWaitRes(byteMerger(byteMerger(FRAME_0,FRAME_1),FlightControlContants.getCommandByIntent(intent)));
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
}
