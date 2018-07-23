package fitme.ai.zotyeautoassistant.bean;

/**
 * Created by gqgz2 on 2018/5/14.
 */

public class ShareApiManagerUrlBean {
    //对话管理器
    private static String DIALOG_MANAGER_URL;
    //消息推送服务
    private static String NOTIFICATION_URL;
    //用户中心
    private static String USER_CENTER_URL;
    //认证服务
    private static String AUTHORIZATION_URL;
    //媒体播放同步接口
    private static String MEDIA_PLAYER_URL;
    //智能硬件
    private static String DEVICE_CONTROL_URL;
    //socket 消息推送
    public static String SOCKET_NOTIFICATION_IP;
    public static int SOCKET_NOTIFICATION_PORT;

    public static String getDialogManagerUrl() {
        return DIALOG_MANAGER_URL;
    }

    public static void setDialogManagerUrl(String dialogManagerUrl) {
        DIALOG_MANAGER_URL = dialogManagerUrl;
    }

    public static String getNotificationUrl() {
        return NOTIFICATION_URL;
    }

    public static void setNotificationUrl(String notificationUrl) {
        NOTIFICATION_URL = notificationUrl;
    }

    public static String getUserCenterUrl() {
        return USER_CENTER_URL;
    }

    public static void setUserCenterUrl(String userCenterUrl) {
        USER_CENTER_URL = userCenterUrl;
    }

    public static String getAuthorizationUrl() {
        return AUTHORIZATION_URL;
    }

    public static void setAuthorizationUrl(String authorizationUrl) {
        AUTHORIZATION_URL = authorizationUrl;
    }

    public static String getMediaPlayerUrl() {
        return MEDIA_PLAYER_URL;
    }

    public static void setMediaPlayerUrl(String mediaPlayerUrl) {
        MEDIA_PLAYER_URL = mediaPlayerUrl;
    }

    public static String getDeviceControlUrl() {
        return DEVICE_CONTROL_URL;
    }

    public static void setDeviceControlUrl(String deviceControlUrl) {
        DEVICE_CONTROL_URL = deviceControlUrl;
    }

    public static String getSocketNotificationIp() {
        return SOCKET_NOTIFICATION_IP;
    }

    public static void setSocketNotificationIp(String socketNotificationIp) {
        SOCKET_NOTIFICATION_IP = socketNotificationIp;
    }

    public static int getSocketNotificationPort() {
        return SOCKET_NOTIFICATION_PORT;
    }

    public static void setSocketNotificationPort(int socketNotificationPort) {
        SOCKET_NOTIFICATION_PORT = socketNotificationPort;
    }
}
