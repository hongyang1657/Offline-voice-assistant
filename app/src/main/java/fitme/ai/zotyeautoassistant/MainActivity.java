package fitme.ai.zotyeautoassistant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import fitme.ai.zotyeautoassistant.bean.ResultBean;
import fitme.ai.zotyeautoassistant.service.ActivateService;
import fitme.ai.zotyeautoassistant.service.NLPMessageService;
import fitme.ai.zotyeautoassistant.service.TtsService;
import fitme.ai.zotyeautoassistant.utils.Constants;
import fitme.ai.zotyeautoassistant.utils.FlightControlContants;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.PushIflytekResToSDCardTask;
import fitme.ai.zotyeautoassistant.utils.TimerUtil;
import fitme.ai.zotyeautoassistant.utils.UDPSocket;
import fitme.ai.zotyeautoassistant.utils.UDPSocketCommand;
import fitme.ai.zotyeautoassistant.utils.UDPSocketRec;
import fitme.ai.zotyeautoassistant.utils.impl.PushResToSDCardListener;
import fitme.ai.zotyeautoassistant.view.impl.TimerEndListener;
import fitme.ai.zotyeautoassistant.view.impl.UdpReceiveListener;

import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_RESPONSE;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_DEFAULT;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_ERROR;
import static fitme.ai.zotyeautoassistant.utils.Constants.ASR_STATE_RESPONSE_TIMEOUT;
import static fitme.ai.zotyeautoassistant.utils.Constants.AWAIT_WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Constants.LOG;
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
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_COMMAND_0;
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_COMMAND_1;

/**
 * 用于众泰车机的语音交互程序
 * attention ：1.使用4G信号，不用wifi
 */

public class MainActivity extends Activity{

    private MBroadcastReceiver mBroadcastReceiver;
    private ExecutorService executorService;
    private Intent intentActivateService = null;
    private Intent intentTtsService = null;
    private Intent intentMessageService = null;
    private ScrollView scrollView;
    private TextView tvLog,tvLogLocal;
    private Context mContext;
    //是否开启连续对话功能
    private boolean isContinuousDialogue = true;
    //是否开启二次确认功能
    private boolean isOpenConfirmation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化
        initView();
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(WAKE_UP_STATE,WAKE_UP);
            }
        });
        tvLog.setVisibility(View.GONE);
        tvLogLocal.setVisibility(View.VISIBLE);
        //讯飞本地模型push到sd卡，否则无法初始化
        new PushIflytekResToSDCardTask(getApplicationContext(), new PushResToSDCardListener() {
            @Override
            public void onComplete() {
                initActivateService();
                initTtsService();
                initMessageService();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20000);
                            sendBroadcast(WAKE_UP_STATE,WAKE_UP);
                            TimerUtil.getInstance().start();    //使用语音唤醒 开始进入倒计时
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).execute();

        initUDPSocket();

        test();
    }

    private void test(){

    }

    private void initUDPSocket(){
        //开启UDP服务端
        UDPSocketRec.getInstance().setOnUdpReceiveListener(new UdpReceiveListener() {
            @Override
            public void onReceiver(byte[] bytes) {
                //Base64解码
                L.i("str主动接受tts：bytes:"+ Arrays.toString(bytes));
                String str = new String(Base64.decode(bytes,Base64.DEFAULT));
                L.i("str主动接受tts：:"+str);
                sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,str);
            }

            @Override
            public void onReceiver(String msg) {

            }
        });


        //开启UDP服务端
        UDPSocket.getInstance().setOnUdpReceiveListener(new UdpReceiveListener() {
            @Override
            public void onReceiver(byte[] bytes) {
                //Base64解码
                String str = new String(Base64.decode(bytes,Base64.DEFAULT));

                sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,str);
            }

            @Override
            public void onReceiver(String msg) {

            }
        });
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


    private void initView(){
        executorService = Executors.newCachedThreadPool(); //线程池
        scrollView = findViewById(R.id.scroll_view);
        tvLog = findViewById(R.id.tv_log_cloud);
        tvLogLocal = findViewById(R.id.tv_log_local);
        TimerUtil.getInstance().setOnTimerEndListener(new TimerEndListener() {
            @Override
            public void timeEnd() {
                L.i("倒计时结束");
            }
        });

        //广播接收
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        registerReceiver(mBroadcastReceiver,intentFilter);
        mContext = this;
    }

    private void initActivateService(){
        intentActivateService = new Intent(this, ActivateService.class);
        startService(intentActivateService);
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
        if (intentTtsService!=null){
            stopService(intentTtsService);
        }
        if (intentMessageService!=null){
            stopService(intentMessageService);
        }
        unregisterReceiver(mBroadcastReceiver);

        System.exit(0);
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
                            control(resultBean.getIntent(),false);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.ENTER_AIR_LINE:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.TAKE_OFF:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.INSTRUCT_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.TURN_LEFT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.TURN_RIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.STRAIGHT_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.CLIMB:
                        if (resultBean.getIntent_score()>0.96f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.LEVEL_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.DECLINE:
                        if (resultBean.getIntent_score()>0.97f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.AUTO_FLIGHT:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.CHECK_ENGINE:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.RAISE_LANDING_GEAR:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.PUT_LANDING_GEAR:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
                        }else {
                            defaultChat();
                        }
                        break;
                    case Constants.STOP_FLYING_CONTROL_SYSTEM:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),false);
                        }else {
                            defaultChat();
                        }
                    case Constants.CLOSE_ENGINE_CONSOLE:
                        if (resultBean.getIntent_score()>0.9f){
                            control(resultBean.getIntent(),true);
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
                                    executiveCommand1(cacheIntent);
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
                            executiveCommand1(Constants.CLOSE_CONFIRMATION);
                            executiveCommand(Constants.CLOSE_CONFIRMATION);
                            isOpenConfirmation = false;
                        }else {
                            defaultChat();
                        }
                        break;
                    case "开启二次确认功能":
                        if (resultBean.getIntent_score()>0.9f){
                            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"已为您打开语音确认功能");
                            executiveCommand1(Constants.OPEN_CONFIRMATION);
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
    private void control(String intent,boolean isControl){
        TimerUtil.getInstance().start();    //使用语音唤醒 开始进入倒计时
        if (isOpenConfirmation){      //需要二次确认
            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"请确认是否要执行"+intent+"操作");
            //TODO 记录用户的控制指令，等待用户确认
            cacheIntent = intent;
        }else {        //不需要二次确认，直接执行命令
            sendBroadcast(TTS_CONTROL,TTS_START,TTS_TEXT,"为您执行"+intent+"操作");
            //TODO 发送UDP指令
            L.i("发送udp");
            if (isControl){
                executiveCommand1(intent);
            }else {
                executiveCommand(intent);
            }
        }
    }

    private void executiveCommand(String intent){
        UDPSocket.getInstance().sendUdpAndWaitRes(byteMerger(byteMerger(FRAME_0,FRAME_1),FlightControlContants.getCommandByIntent(intent)));
    }

    private void executiveCommand1(String intent){
        UDPSocketCommand.getInstance().sendUdpAndWaitRes(byteMerger(byteMerger(FRAME_COMMAND_0,FRAME_COMMAND_1),FlightControlContants.getCommandByIntent(intent)));
    }


    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
}
