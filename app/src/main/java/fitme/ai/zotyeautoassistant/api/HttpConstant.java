package fitme.ai.zotyeautoassistant.api;

/**
 * Created by hongy on 2017/10/18.
 */

public class HttpConstant {
    public static final String API_VERSION = "2.0";
    ////////////////////////////////////////////////////////// 用户中心 /////////////////////////////////////////////////////////////////////////
    //     验证手机号是否已被占用
    public static final String METHOD_UC_IS_OCCUPIED_MOBILE = "account/is_occupied_mobile";
    //    创建用户
    public static final String METHOD_UC_CREATE = "account/create";
    //    请求向指定手机发送用于验证手机的验证码
    public static final String METHOD_UC_TO_MOBILE_VERIFY_CODE_CREATE = "account/to_mobile_verify_code/create";
    //    验证用户手机
    public static final String METHOD_UC_VERIFY_MOBILE = "account/verify_mobile";
    //    修改密码
    public static final String METHOD_UC_CHANGE_PASSWORD = "account/change_password";
    //    向指定手机发送用于重置密码的验证码
    public static final String METHOD_UC_RESET_PASSWORD_VERIFY_CODE_CREATE = "account/reset_password_verify_code/create";
    //    重置密码
    public static final String METHOD_UC_RESET_PASSWORD = "account/reset_password";
    //    第三方登录
    public static final String METHOD_UC_THIRD_PARTY_LOGIN = "account/third_party_login";
    //    获取用户自己的信息
    public static final String METHOD_UC_PROFILE = "profile";
    //    设置用户自己的信息
    public static final String METHOD_UC_PROFILE_PUT = "profile/put";
    //    按手机号查询user_id
    public static final String METHOD_UC_GET_USER_ID_BY_MOBILE = "get_user_id_by_mobile";
    //    交互设备设置
    public static final String METHOD_UC_DEVICE_PUT = "device/put";
    //    提交反馈
    public static final String METHOD_UC_FEEDBACK_CREATE = "feedback/create";
    //    分享
    public static final String METHOD_UC_SHARE_CREATE = "share/create";
    //    终端上传异常日志
    public static final String METHOD_UC_ERROR_LOG_CREATE = "error_log/create";
    ////////////////////////////////////////////////////////// 认证服务 /////////////////////////////////////////////////////////////////////////
    //    获取访问令牌
    public static final String METHOD_AT_TOKEN = "token";
    ////////////////////////////////////////////////////////// 消息推送服务 /////////////////////////////////////////////////////////////////////////
    //    推送消息给用户
    public static final String METHOD_NC_MSG_TO_USER_CREATE = "message/to_user/create";
    //    获取发给消费者的新消息
    public static final String METHOD_NC_MSG_TO_USER = "message/to_user";
    //    确认消息已收到
    public static final String METHOD_NC_MSG_ARRIVED_CREATE = "message_arrived/create";
    ////////////////////////////////////////////////////////// 对话管理 /////////////////////////////////////////////////////////////////////////
    //    新增来自用户的消息
    public static final String METHOD_DM_MSG_FROM_USER_CREATE = "message/from_user/create";
    //     清除上下文的测试接口
    public static final String METHOD_DM_CLOSE_DIALOG = "close_dialog";
    //     检查当前设备的配置信息是否有更新
    public static final String METHED_DEVICE_CONFIG_CHECK = "device_config/cheak";
    //     获取指定交互设备的配置信息
    public static final String METHED_DEVICE_CONFIG_GET= "device_config/get";
    //     设置指定交互设备的配置节点信息
    public static final String METHED_DEVICE_CONFIG_PUT= "device_config/put";
    ////////////////////////////////////////////////////////// 获取已经绑定过的品牌账号 /////////////////////////////////////////////////////////////////////////
    //     获取账号
    public static final String METHOD_SD_ACCOUNT_GET = "account/get";

    //     环境信息上传
    public static final String METHOD_SD_ENVIRONMENT_STATE_UPLOAD = "environment/state/upload";

    public static final String INSTANCE_NAME = "production";
    public static final String DOMAIN_NAME = "app.fitme.ai";
    public static final String DOMAIN_PORT = "9999";
}
