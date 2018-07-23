package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by hongy on 2018/2/2.
 */

public class AMapLocationUtils {

    private static Context mContext;

    private static class AMapLocationHolder{
        private static  AMapLocationUtils instances = new AMapLocationUtils();
    }

    public AMapLocationUtils(){
    }

    public static AMapLocationUtils getInstances(Context context){
        mContext = context.getApplicationContext();
        return AMapLocationHolder.instances;
    }

    //初始化高德定位
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public void initLocation(){
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(com.amap.api.location.AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        int longtitude = (int)(aMapLocation.getLongitude()*10000000);
                        int latitude = (int) (aMapLocation.getLatitude()*10000000);
                        L.i("高德定位经纬度："+longtitude+"----"+latitude+"  ");
                        SharedPreferencesUtils.getInstance().setIntKeyValue(SPConstants.SP_AYAH_LONGTITUDE,longtitude);
                        SharedPreferencesUtils.getInstance().setIntKeyValue(SPConstants.SP_AYAH_LATITUDE,latitude);
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        L.i("AmapError:"+"location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }


            }
        });

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。不会使用GPS和其他传感器，只会使用网络定位
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //定位间隔
        mLocationOption.setInterval(5000);
        mLocationOption.setOnceLocation(true);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }
}
