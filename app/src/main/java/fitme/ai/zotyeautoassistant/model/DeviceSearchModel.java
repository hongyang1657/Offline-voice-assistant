package fitme.ai.zotyeautoassistant.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.DevicesInfo;
import fitme.ai.zotyeautoassistant.model.impl.IDeviceSearchModel;
import fitme.ai.zotyeautoassistant.presenter.DeviceSearchPresenter;
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

public class DeviceSearchModel implements IDeviceSearchModel {

    private DeviceSearchPresenter deviceSearchPresenter;

    public DeviceSearchModel(DeviceSearchPresenter deviceSearchPresenter){
        this.deviceSearchPresenter = deviceSearchPresenter;
    }

    @Override
    public void deviceSearch(Context context) {
        String userId= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.user_center_api_key;
        String apiSecret= ApiManager.user_center_api_secret;
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "device");
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("device_id", Mac.getMac());

        Gson gson = new Gson();
        L.i("http_bodyhttp_bodyhttp_bodyhttp_body:"+gson.toJson(map));
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.UserCenterService.deviceSearch(apikey, timeStamp, sign,HttpConstant.API_VERSION,"device",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DevicesInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("交互设备查询失败:"+e.toString());
                    }
                    @Override
                    public void onNext(DevicesInfo jsonObject) {
                        deviceSearchPresenter.onSucess(jsonObject);
                    }
                });
    }

    public interface deviceSearchListener{
        void onSucess(DevicesInfo devicesInfo);
        void onFailure();
    }
}
