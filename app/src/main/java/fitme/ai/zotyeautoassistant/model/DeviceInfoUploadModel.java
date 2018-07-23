package fitme.ai.zotyeautoassistant.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;


import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.impl.IDeviceInfoUploadModel;
import fitme.ai.zotyeautoassistant.presenter.DeviceInfoUploadPresenter;
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

public class DeviceInfoUploadModel implements IDeviceInfoUploadModel {

    private DeviceInfoUploadPresenter deviceInfoUploadPresenter;

    public DeviceInfoUploadModel(DeviceInfoUploadPresenter deviceInfoUploadPresenter){
        this.deviceInfoUploadPresenter = deviceInfoUploadPresenter;
    }

    @Override
    public void deviceInfoUpload(Context context) {
        String userId= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        L.i("tokennnnnn:"+token);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.user_center_api_key;
        String apiSecret= ApiManager.user_center_api_secret;
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", HttpConstant.METHOD_UC_DEVICE_PUT);
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("token", token);
        map.put("device_id", Mac.getMac());
        map.put("device_type","smart_speaker");

        Gson gson = new Gson();
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.UserCenterService.deviceInfoUpload(apikey, timeStamp, sign,HttpConstant.API_VERSION, HttpConstant.METHOD_UC_DEVICE_PUT,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("上传设备信息错误:"+e.toString());
                        deviceInfoUploadPresenter.onFailure();
                    }
                    @Override
                    public void onNext(Status jsonObject) {
                        deviceInfoUploadPresenter.onSucess(jsonObject);
                    }
                });
    }

    public interface deviceInfoUploadListener{
        void onSucess(Status responseBody);
        void onFailure();
    }
}
