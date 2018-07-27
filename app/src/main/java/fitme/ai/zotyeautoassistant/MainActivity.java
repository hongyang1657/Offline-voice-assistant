package fitme.ai.zotyeautoassistant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
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
import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.bean.DevicesInfo;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.bean.TokenInfo;
import fitme.ai.zotyeautoassistant.presenter.DeviceBindPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceBindSuccessPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceConfigPutPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceInfoUploadPresenter;
import fitme.ai.zotyeautoassistant.presenter.DeviceSearchPresenter;
import fitme.ai.zotyeautoassistant.presenter.GetDeviceConfigPresenter;
import fitme.ai.zotyeautoassistant.presenter.GetUserIdByMobilePresenter;
import fitme.ai.zotyeautoassistant.presenter.TokenPresenter;
import fitme.ai.zotyeautoassistant.service.AsrService;
import fitme.ai.zotyeautoassistant.service.ActivateService;
import fitme.ai.zotyeautoassistant.service.NLPMessageService;
import fitme.ai.zotyeautoassistant.service.TtsService;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.Mac;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;
import okhttp3.ResponseBody;

import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_RESPONSE;
import static fitme.ai.zotyeautoassistant.utils.Contansts.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Contansts.LOG;
import static fitme.ai.zotyeautoassistant.utils.Contansts.LOGIN_STATE;

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
    private TextView tvLog;
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
    //获取设备配置信息
    private GetDeviceConfigPresenter getDeviceConfigPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executorService = Executors.newCachedThreadPool(); //线程池
        findViewById(R.id.bt_show_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().getmFloatingView().show();
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
        //初始化
        initView();
        initPresenter();
        initActivateService();
        initAsrService();
        initTtsService();
        initMessageService();

        mContext = this;
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
        getDeviceConfigPresenter = new GetDeviceConfigPresenter(this);

        //广播接收
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        registerReceiver(mBroadcastReceiver,intentFilter);
    }

    private void initView(){
        scrollView = findViewById(R.id.scroll_view);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        tvLog = findViewById(R.id.tv_log);
        SharedPreferencesUtils.getInstance().setIntKeyValue(SPConstants.SP_AYAH_LONGTITUDE,1202106130);
        SharedPreferencesUtils.getInstance().setIntKeyValue(SPConstants.SP_AYAH_LATITUDE,302644590);
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
    }

    private class MBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String log = intent.getStringExtra(LOG);
            String asrResponse = intent.getStringExtra(ASR_RESPONSE);
            if (null!=log&&!"{\"messages\":[],\"status\":\"success\"}".equals(log)){
                if (tvLog.getTextSize() < 50000){
                    tvLog.append(formatJson(log)+"\n");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                }else {
                    tvLog.setText(formatJson(log)+"\n");
                }
            }
            if (null!=asrResponse&&!asrResponse.equals("")){
                tvLog.append("\nASR:"+asrResponse+"\n");
            }
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
    public void getDeviceConfig(DeviceConfigBean responseBody) {

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
}
