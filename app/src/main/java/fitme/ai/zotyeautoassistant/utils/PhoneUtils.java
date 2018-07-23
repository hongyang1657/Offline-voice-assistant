package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by hongy on 2017/10/23.
 */

public class PhoneUtils {
    /**
     * 安卓系统版本号
     */
    private String systemVer = "";
    /**
     * 平台版本号
     */
    private String appstoreVer = "";
    private String imsi = "";
    private String iccid = "";
    private String imei = "";
    private String deviceId = "";
    /**
     * 手机型号
     */
    private String model = "";
    /**
     * 厂商
     */
    private String manufacturer = "";
    /**
     * 屏幕宽度（像素）
     */
    private String scrw = "0";
    /**
     * 屏幕高度（像素）
     */
    private String scrh = "0";
    /**
     * 屏幕密度（0.75 / 1.0 / 1.5）
     */
    private String scrDensity = "";
    /**
     * 屏幕密度DPI（120 / 160 / 240）
     */
    private String scrDensityDpi = "";
    private DisplayMetrics metric = null;
    private static PhoneUtils phoneinfo = null;
    private Context ctx = null;
    /**
     * 实例化对象
     *
     * @param ctx
     * @return
     */
    public static PhoneUtils init(Context ctx) {
        if (null == phoneinfo) return getValueFromPhone(ctx);
        if (null == phoneinfo.metric) phoneinfo.initMetric(ctx);
        return phoneinfo;
    }
    public Context getCtx() {
        return ctx;
    }
    public static PhoneUtils getInfo() {
        return phoneinfo;
    }
    /* 获得手机型号 */
    public String getPhoneModel() {
        if (beKong(model)) {
            this.model = Build.MODEL;
        }
        return model;
    }
    /*
     * 获得mac address
     */
    public String getMacAddress() {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
    /* 获得制造商 */
    public String getManufacturer() {
        if (beKong(manufacturer)) {
            this.manufacturer = Build.MANUFACTURER;
        }
        return manufacturer;
    }
    /* 获得安卓系统版本号 */
    public String getSystemVer() {
        if (beKong(systemVer)) {
            this.systemVer = Build.VERSION.RELEASE;
        }
        return systemVer;
    }
    /* 获得平台版本号 */
    public String getAppstoreVer() {
        if (beKong(appstoreVer)) {
            try {
                PackageManager manager = ctx.getPackageManager();
                PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
                String version = info.versionName;
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return "get versionname error";
            }
        }
        return appstoreVer;
    }
    /* 获得IMEI */
    public String getImei() {
        if (beKong(imei)) {
            try {
                String imeiStr = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (!beKong(imeiStr)) imei = imeiStr;
            } catch (Exception e) {
            }
        }
        return imei;
    }
    /* 获得IMSI */
    public String getImsi() {
        if (beKong(imsi)) {
            try {
                String imsiStr = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
                if (!beKong(imsiStr)) imsi = imsiStr;
            } catch (Exception e) {
            }
        }
        return imsi;
    }
    /* 获得ICCID */
    public String getIccid() {
        if (beKong(iccid)) {
            try {
                String iccidStr = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
                if (!beKong(iccidStr)) iccid = iccidStr;
            } catch (Exception e) {
            }
        }
        return iccid;
    }
    public String getDeviceId(){
        if (beKong(deviceId)) {
            try {
                String deviceIdStr = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (!beKong(deviceIdStr)) deviceId = deviceIdStr;
            } catch (Exception e) {
            }
        }
        return deviceId;
    }
    public String getScreenWidth() {
        return scrw;
    }
    public String getScreenHeight() {
        return scrh;
    }
    public String getScreenDensity() {
        return scrDensity;
    }
    public String getScreenDensityDpi() {
        return scrDensityDpi;
    }
    /**
     * 初始化屏幕数据
     *
     * @param ctx
     * @return
     */
    public DisplayMetrics initMetric(Context ctx) {
        try {
            if (null == metric) metric = new DisplayMetrics();
            if (beKongWith0(scrw) || beKongWith0(scrh) || beKongWith0(scrDensity) || beKongWith0(scrDensityDpi)) {
                WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
                wm.getDefaultDisplay().getMetrics(metric);
                scrw = String.valueOf(metric.widthPixels);
                scrh = String.valueOf(metric.heightPixels);
                scrDensity = String.valueOf(metric.density);
                scrDensityDpi = String.valueOf(metric.densityDpi);
            }
        } catch (Exception e) {
        }
        return metric;
    }
    public static PhoneUtils getValueFromPhone(Context ctx) {
        if (null == phoneinfo) phoneinfo = new PhoneUtils();
        if (null == ctx) return phoneinfo;
        phoneinfo.ctx = ctx;
        // 验证ctx
        if (beKong(phoneinfo.imsi)) {
            try {
                String imsi = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
                if (!beKong(imsi)) {
                    phoneinfo.imsi = imsi;
                    // ctx有效
                    phoneinfo.ctx = ctx;
                    // 初始化所有的值
                    // 2
                    phoneinfo.getImei();
                    // 3
                    phoneinfo.getIccid();
                    // 4平台版本号
                    phoneinfo.getAppstoreVer();
                    // 5安卓系统版本号
                    phoneinfo.getSystemVer();
                    // 6手机型号
                    phoneinfo.getPhoneModel();
                    // 7手机制造商
                    phoneinfo.getManufacturer();
                    // //8手机厂家编号
//                     phoneinfo.getCompany();
                    phoneinfo.getDeviceId();
                }
            } catch (Exception e) {
            }
        }
        // 初始化化屏幕尺寸
        phoneinfo.initMetric(ctx);
        L.logE("___Phone:__appstoreVer:"+phoneinfo.appstoreVer+"__imsi:"+phoneinfo.imsi+"___ctx:"+phoneinfo.ctx
                +"___imei:"+phoneinfo.imei+"___iccid:"+phoneinfo.iccid+"____manufacturer:"+phoneinfo.manufacturer
                +"__systemVer:"+phoneinfo.systemVer+"___model:"+phoneinfo.model+"___deviceId:"+phoneinfo.deviceId);
        return phoneinfo;
    }
    /**
     * 判断字符串是否为null，或者为空字符串
     *
     * @param str
     * @return
     */
    public static boolean beKong(String str) {
        if (null != str) {
            return "".equals(str.trim());
        }
        return true;
    }
    /**
     * 判断字符串是否为null、字符串空或者为字符串0
     *
     * @param str
     * @return
     */
    public static boolean beKongWith0(String str) {
        if (null != str) {
            return "".equals(str.trim()) || "0".equals(str.trim());
        }
        return true;
    }
    // 获取手机通话时长
//    public static long getCallTime(Context context){
//        Cursor cursor = context.getContentResolver ().query (Calls.CONTENT_URI, new String[] { Calls.DURATION, Calls.TYPE, Calls.DATE }, null, null,
//                Calls.DEFAULT_SORT_ORDER);
//        boolean hasRecord = cursor.moveToFirst ();
//        long incoming = 0L;
//        long outgoing = 0L;
//        while (hasRecord) {
//            int type = cursor.getInt (cursor.getColumnIndex (Calls.TYPE));
//            long duration = cursor.getLong (cursor.getColumnIndex (Calls.DURATION));
//            switch (type) {
//                case Calls.INCOMING_TYPE:
//                    incoming += duration;
//                    break;
//                case Calls.OUTGOING_TYPE:
//                    outgoing += duration;
//                default:
//                    break;
//            }
//            hasRecord = cursor.moveToNext ();
//        }
//        cursor.close ();
//
//        return (incoming + outgoing);
//    }
}
