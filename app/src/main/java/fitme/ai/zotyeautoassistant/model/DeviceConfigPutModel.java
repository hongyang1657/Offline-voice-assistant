package fitme.ai.zotyeautoassistant.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.model.impl.IDeviceConfigPutModel;
import fitme.ai.zotyeautoassistant.presenter.DeviceConfigPutPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.Mac;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 上传手机设备信息
 * Created by yml on 2017/10/18.
 */

public class DeviceConfigPutModel implements IDeviceConfigPutModel {

    private DeviceConfigPutPresenter deviceConfigPutPresenter;

    public DeviceConfigPutModel(DeviceConfigPutPresenter deviceConfigPutPresenter){
        this.deviceConfigPutPresenter = deviceConfigPutPresenter;
    }

    @Override
    public void deviceConfigPut(Context context, String path, Object value) {
        String userId= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.user_center_api_key;
        String apiSecret= ApiManager.user_center_api_secret;
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "device_config/put");
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("token", token);
        map.put("device_id", Mac.getMac());
        map.put("path",path);
        map.put("value",value);

        Gson gson = new Gson();
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.UserCenterService.deviceConfigPut(apikey, timeStamp, sign, HttpConstant.API_VERSION, "device_config/put",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("上传设备信息错误:"+e.toString());
                        deviceConfigPutPresenter.onFailure();
                    }
                    @Override
                    public void onNext(ResponseBody jsonObject) {
                        deviceConfigPutPresenter.onSucess(jsonObject);
                    }
                });
    }

    public interface deviceConfigPutListener{
        void onSucess(ResponseBody responseBody);
        void onFailure();
    }
}
