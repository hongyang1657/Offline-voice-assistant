package fitme.ai.zotyeautoassistant.api;


import java.util.concurrent.TimeUnit;

import fitme.ai.zotyeautoassistant.utils.AllocatePortUtil;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class ApiManager {

    //二代Fitme的api访问接口地址
    public static final String VERSION = "2.0";

    /**
     * local 环境
     */
    //对话管理器
//    private static final String DIALOG_MANAGER_URL = "http://172.16.12.195:7050";
//    //消息推送服务
//    private static final String NOTIFICATION_URL = "http://172.16.12.195:9001";
//    //用户中心
//    private static final String USER_CENTER_URL = "http://172.16.12.181:7070";
//    //认证服务
//    private static final String AUTHORIZATION_URL = "http://172.16.12.195:7060";
//    //媒体播放同步接口
//    private static final String MEDIA_PLAYER_URL = "http://172.16.12.181:20100";
//    //智能硬件
//    private static final String DEVICE_CONTROL_URL="http://172.16.12.176:20072";
//    //智能场景
//    private static final String SMART_SCENE_URL="http://172.16.12.176:20073";
//    //socket 消息推送
//    public static final String SOCKET_NOTIFICATION_IP = "app.fitme.ai";
//    public static final int SOCKET_NOTIFICATION_PORT = 1111;

    /**
     *  自动化打包动态参数
     */
    //对话管理器
    private static final String DIALOG_MANAGER_URL = "http://dialogmanage."+ HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME+":"+HttpConstant.DOMAIN_PORT;
    //消息推送服务
    private static final String NOTIFICATION_URL = "http://notification."+ HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME+":"+HttpConstant.DOMAIN_PORT;
    //用户中心
    private static final String USER_CENTER_URL = "http://usercenter."+ HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME+":"+HttpConstant.DOMAIN_PORT;
    //认证服务
    private static final String AUTHORIZATION_URL = "http://authentication."+ HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME+":"+HttpConstant.DOMAIN_PORT;
    //媒体播放同步接口
    private static final String MEDIA_PLAYER_URL = "http://contentservice."+ HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME+":"+HttpConstant.DOMAIN_PORT;
    //智能硬件
    private static final String DEVICE_CONTROL_URL= "http://smarthardwarecontrol."+ HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME+":"+HttpConstant.DOMAIN_PORT;
    //智能场景
    private static final String SMART_SCENE_URL = "http://smartscen."+ HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME+":"+HttpConstant.DOMAIN_PORT;
    //socket 消息推送
    public static final String SOCKET_NOTIFICATION_IP = "pushserver."+HttpConstant.INSTANCE_NAME+"."+HttpConstant.DOMAIN_NAME;
    public static final int SOCKET_NOTIFICATION_PORT = AllocatePortUtil.allocatePort(HttpConstant.INSTANCE_NAME+"pushserver");



    //ApiKey
    public static final String dialog_manage_api_key = "dialog_manage";
    public static final String notification_api_key = "notification";
    public static final String user_center_api_key = "user_center";
    public static final String authorization_api_key = "authorization";
    public static final String atom_service_api_key = "atom_service";


    //ApiSecret
    public static final String dialog_manage_api_secret = "hd78fceaf5d445409610398d83088527";
    public static final String notification_api_secret = "963f926f3e4947e99d147b3c451abd6d";
    public static final String user_center_api_secret = "ga78fceaf5d445409610398d83088528";
    public static final String authorization_api_secret = "fb78fceaf5d445409610398d83088529";
    public static final String atom_service_api_secret = "as78fceaf5d446789610398d83088528";

    //okhttp客户端
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build();


    //二代对话管理器retrofit服务接口
    private static final Retrofit retrofit_dialog_manager = new Retrofit.Builder()
            .baseUrl(DIALOG_MANAGER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService DialogManagerService = retrofit_dialog_manager.create(ApiService.class);


    //二代消息推送服务retrofit服务接口
    private static final Retrofit retrofit_notification = new Retrofit.Builder()
            .baseUrl(NOTIFICATION_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService NotificationService = retrofit_notification.create(ApiService.class);


    //二代用户中心retrofit服务接口
    private static final Retrofit retrofit_user_center = new Retrofit.Builder()
            .baseUrl(USER_CENTER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService UserCenterService = retrofit_user_center.create(ApiService.class);


    //认证服务retrofit服务接口
    private static final Retrofit retrofit_authorization = new Retrofit.Builder()
            .baseUrl(AUTHORIZATION_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService AuthorizationService = retrofit_authorization.create(ApiService.class);

    //二代媒体播放器同步接口
    private static final Retrofit retrofit_media_player = new Retrofit.Builder()
            .baseUrl(MEDIA_PLAYER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService MediaPlayerService = retrofit_media_player.create(ApiService.class);

    //硬件设备控制
    private static final Retrofit retrofit_device_control = new Retrofit.Builder()
            .baseUrl(DEVICE_CONTROL_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService deviceControlService = retrofit_device_control.create(ApiService.class);

    //硬件场景控制
    private static final Retrofit retrofit_smart_scene = new Retrofit.Builder()
            .baseUrl(SMART_SCENE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService smartSceneService = retrofit_smart_scene.create(ApiService.class);

}
