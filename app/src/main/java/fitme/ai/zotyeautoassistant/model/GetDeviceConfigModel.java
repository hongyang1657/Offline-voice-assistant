package fitme.ai.zotyeautoassistant.model;

import com.google.gson.Gson;

import java.util.HashMap;


import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.presenter.GetDeviceConfigPresenter;
import fitme.ai.zotyeautoassistant.presenter.impl.IGetDeviceConfigPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.Mac;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hongy on 2017/12/22.
 */

public class GetDeviceConfigModel implements IGetDeviceConfigPresenter {

    private GetDeviceConfigPresenter getDeviceConfigPresenter;

    public GetDeviceConfigModel(GetDeviceConfigPresenter getDeviceConfigPresenter) {
        this.getDeviceConfigPresenter = getDeviceConfigPresenter;
    }

    @Override
    public void getDeviceConfig() {
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token =SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "device_config/get");
        params.put("api_key", ApiManager.user_center_api_key);
        params.put("timestamp", timeStamp);
        params.put("version",ApiManager.VERSION);

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("device_id", Mac.getMac());
        map.put("token", token);

        Gson gson = new Gson();
        params.put("http_body",gson.toJson(map));

        String sign = SignAndEncrypt.signRequest(params, ApiManager.user_center_api_secret);
        ApiManager.UserCenterService.deviceConfigGet(ApiManager.user_center_api_key, timeStamp,sign,ApiManager.VERSION,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceConfigBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("device_config/get:error:"+e.toString());
                    }

                    @Override
                    public void onNext(DeviceConfigBean deviceConfigBean) {
                        getDeviceConfigPresenter.onSucess(deviceConfigBean);
                    }
                });
    }

    public interface getDeviceConfigListener{
        void onSucess(DeviceConfigBean responseBody);
        void onFailure();
    }
}
