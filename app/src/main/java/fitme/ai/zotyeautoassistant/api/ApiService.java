package fitme.ai.zotyeautoassistant.api;


import java.util.Map;

import fitme.ai.zotyeautoassistant.bean.AccountCreate;
import fitme.ai.zotyeautoassistant.bean.AccountGet;
import fitme.ai.zotyeautoassistant.bean.CheckToken;
import fitme.ai.zotyeautoassistant.bean.ClearContextResp;
import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.bean.DevicesInfo;
import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.bean.StatusWithUrl;
import fitme.ai.zotyeautoassistant.bean.TokenInfo;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by blw on 2016/8/26.
 */
public interface ApiService {



    //设备绑定,绑定智能音箱
    @POST("account/device/create")
    Observable<ResponseBody> deviceBind(@Query("api_key") String apiKey,
                                        @Query("timestamp") String timestamp,
                                        @Query("sign") String sign,
                                        @Body Map<String, Object> http_body);


    /**
     * 二代接口方法
     */

    //根据手机号获取用户id
    @POST("get_user_id_by_mobile")
    Observable<AccountCreate> getUserIdByMobile(@Query("api_key") String apiKey,
                                                @Query("timestamp") String timestamp,
                                                @Query("sign") String sign,
                                                @Query("version") String version,
                                                @Body Map<String, Object> http_body);

    //登录功能，即获取用户信息功能
    @POST("token")
    Observable<TokenInfo> token(@Query("api_key") String apiKey,
                                @Query("timestamp") String timestamp,
                                @Query("sign") String sign,
                                @Query("version") String version,
                                @Query("method") String method,
                                @Body Map<String, Object> http_body);

    //鉴定token
    @POST("check_token")
    Observable<CheckToken> checkToken(@Query("api_key") String apiKey,
                                      @Query("timestamp") String timestamp,
                                      @Query("sign") String sign,
                                      @Query("version") String version,
                                      @Query("method") String method,
                                      @Body Map<String, Object> http_body);

    //新增用户消息
    @POST("message/from_user/create")
    Observable<Status> messageCreate(@Query("api_key") String apiKey,
                                     @Query("timestamp") String timestamp,
                                     @Query("sign") String sign,
                                     @Query("version") String version,
                                     @Body Map<String, Object> http_body);

    //获取服务器回复的消息
    @POST("message/to_user")
    Observable<MessageGet> getMessage(@Query("api_key") String apiKey,
                                      @Query("timestamp") String timestamp,
                                      @Query("sign") String sign,
                                      @Query("version") String version,
                                      @Body Map<String, Object> http_body);

    //确认消息收到
    @POST("message_arrived/create")
    Observable<Status> messageArrived(@Query("api_key") String apiKey,
                                      @Query("timestamp") String timestamp,
                                      @Query("sign") String sign,
                                      @Query("version") String version,
                                      @Body Map<String, Object> http_body);

    // 上传交互设备
    @POST("device/put")
    Observable<Status> deviceInfoUpload(@Query("api_key") String apiKey,
                                        @Query("timestamp") String timestamp,
                                        @Query("sign") String sign,
                                        @Query("version") String version,
                                        @Query("method") String method,
                                        @Body Map<String, Object> http_body);

    //绑定交互设备device/bind
    @POST("device/bind")
    Observable<Status> deviceBind(@Query("api_key") String apiKey,
                                  @Query("timestamp") String timestamp,
                                  @Query("sign") String sign,
                                  @Query("version") String version,
                                  @Query("method") String method,
                                  @Body Map<String, Object> http_body);

    //确认交互设备已绑定成功device/bind/success
    @POST("device/bind/success")
    Observable<Status> deviceBindSuccess(@Query("api_key") String apiKey,
                                         @Query("timestamp") String timestamp,
                                         @Query("sign") String sign,
                                         @Query("version") String version,
                                         @Query("method") String method,
                                         @Body Map<String, Object> http_body);

    //交互设备查询（查询账号所绑定的所有设备）
    @POST("device")
    Observable<DevicesInfo> deviceSearch(@Query("api_key") String apiKey,
                                         @Query("timestamp") String timestamp,
                                         @Query("sign") String sign,
                                         @Query("version") String version,
                                         @Query("method") String method,
                                         @Body Map<String, Object> http_body);

    //设备控制状态上传
    @POST("control/state/upload")
    Observable<ResponseBody> uploadControlState(@Query("api_key") String apiKey,
                                                @Query("timestamp") String timestamp,
                                                @Query("sign") String sign,
                                                @Query("version") String version,
                                                @Body Map<String, Object> http_body);

    //获取账号
    @POST(HttpConstant.METHOD_SD_ACCOUNT_GET)
    Observable<AccountGet> accountGet(@Query("api_key") String apiKey,
                                      @Query("timestamp") String timestamp,
                                      @Query("sign") String sign,
                                      @Query("version") String version,
                                      @Body Map<String, Object> http_body);


    //获取指定交互设备的配置信息
    @POST(HttpConstant.METHED_DEVICE_CONFIG_GET)
    Observable<DeviceConfigBean> deviceConfigGet(@Query("api_key") String apiKey,
                                                 @Query("timestamp") String timestamp,
                                                 @Query("sign") String sign,
                                                 @Query("version") String version,
                                                 @Body Map<String, Object> http_body);

    //检查当前设备的配置信息是否有更新
    @POST(HttpConstant.METHED_DEVICE_CONFIG_CHECK)
    Observable<ResponseBody> deviceConfigCheck(@Query("api_key") String apiKey,
                                               @Query("timestamp") String timestamp,
                                               @Query("sign") String sign,
                                               @Query("version") String version,
                                               @Body Map<String, Object> http_body);

    //设置指定交互设备的配置节点信息
    @POST(HttpConstant.METHED_DEVICE_CONFIG_PUT)
    Observable<ResponseBody> deviceConfigPut(@Query("api_key") String apiKey,
                                             @Query("timestamp") String timestamp,
                                             @Query("sign") String sign,
                                             @Query("version") String version,
                                             @Query("method") String method,
                                             @Body Map<String, Object> http_body);



    //终端上传异常日志
    @POST("error_log/create")
    Observable<Status> errorLogCreat(@Query("api_key") String apiKey,
                                     @Query("timestamp") String timestamp,
                                     @Query("sign") String sign,
                                     @Query("version") String version,
                                     @Query("method") String method,
                                     @Body Map<String, Object> http_body);


    /**
     * 媒体播放同步接口
     */
    @POST("player/next")
    Observable<StatusWithUrl> mediaNext(@Query("api_key") String apiKey,
                                        @Query("timestamp") String timestamp,
                                        @Query("sign") String sign,
                                        @Query("version") String version,
                                        @Query("method") String method,
                                        @Body Map<String, Object> http_body);

    @POST("player/playprevious")
    Observable<ResponseBody> mediaPlayPrevious(@Query("api_key") String apiKey,
                                               @Query("timestamp") String timestamp,
                                               @Query("sign") String sign,
                                               @Query("version") String version,
                                               @Query("method") String method,
                                               @Body Map<String, Object> http_body);

    @POST("player/pause")
    Observable<Status> mediaPause(@Query("api_key") String apiKey,
                                  @Query("timestamp") String timestamp,
                                  @Query("sign") String sign,
                                  @Query("version") String version,
                                  @Query("method") String method,
                                  @Body Map<String, Object> http_body);

    @POST("player/resume")
    Observable<Status> mediaContinue(@Query("api_key") String apiKey,
                                     @Query("timestamp") String timestamp,
                                     @Query("sign") String sign,
                                     @Query("version") String version,
                                     @Query("method") String method,
                                     @Body Map<String, Object> http_body);

    @POST("player/report/status")
    Observable<Status> mediaReport(@Query("api_key") String apiKey,
                                   @Query("timestamp") String timestamp,
                                   @Query("sign") String sign,
                                   @Query("version") String version,
                                   @Query("method") String method,
                                   @Body Map<String, Object> http_body);



    /**
     * 执行场景
     */
    @POST("scene/activate")
    Observable<ResponseBody> sceneActivate(@Query("api_key") String apiKey,
                                           @Query("timestamp") String timestamp,
                                           @Query("sign") String sign,
                                           @Query("version") String version,
                                           @Query("method") String method,
                                           @Body Map<String, Object> http_body);
}