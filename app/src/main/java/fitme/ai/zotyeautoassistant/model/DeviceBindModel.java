package fitme.ai.zotyeautoassistant.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;


import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.impl.IDeviceBindModel;
import fitme.ai.zotyeautoassistant.presenter.DeviceBindPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.Mac;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 上传手机设备信息
 * Created by yml on 2017/10/18.
 */

public class DeviceBindModel implements IDeviceBindModel {

    private DeviceBindPresenter deviceBindPresenter;

    public DeviceBindModel(DeviceBindPresenter deviceBindPresenter){
        this.deviceBindPresenter = deviceBindPresenter;
    }

    @Override
    public void deviceBind(Context context) {
        String userId= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.user_center_api_key;
        String apiSecret= ApiManager.user_center_api_secret;
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "device/bind");
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("device_id", Mac.getMac());
        map.put("token", token);

        Gson gson = new Gson();
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.UserCenterService.deviceBind(apikey, timeStamp, sign, HttpConstant.API_VERSION,"device/bind",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("绑定设备失败:"+e.toString());
                    }
                    @Override
                    public void onNext(Status jsonObject) {
                        deviceBindPresenter.onSucess(jsonObject);
                    }
                });
    }

    public interface deviceBindListener{
        void onSucess(Status responseBody);
        void onFailure();
    }
}
