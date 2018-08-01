package fitme.ai.zotyeautoassistant.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedHashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.presenter.SmartSceneControlPresenter;
import fitme.ai.zotyeautoassistant.presenter.impl.ISmartSceneControlPresenter;
import fitme.ai.zotyeautoassistant.utils.CreateMessageIdUtils;
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
 * Created by hongy on 2017/12/22.
 */

public class SmartSceneControlModel implements ISmartSceneControlPresenter {
    private SmartSceneControlPresenter smartSceneControlPresenter;

    public SmartSceneControlModel(SmartSceneControlPresenter smartSceneControlPresenter) {
        this.smartSceneControlPresenter = smartSceneControlPresenter;
    }

    @Override
    public void sceneActivate(String words, Context context) {
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token =SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String apikey= ApiManager.user_center_api_key;
        String apiSecret= ApiManager.user_center_api_secret;
        String messageId = CreateMessageIdUtils.getMessageId(userId,context);
        int longtitude = SharedPreferencesUtils.getInstance().getIntValueByKey(SPConstants.SP_AYAH_LONGTITUDE);
        int latitude = SharedPreferencesUtils.getInstance().getIntValueByKey(SPConstants.SP_AYAH_LATITUDE);

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("method","scene/activate");
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);


        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user_id", userId);
        map.put("message_id", messageId);
        map.put("device_id", Mac.getMac());
        map.put("words", words);
        map.put("x", longtitude);
        map.put("y", latitude);
        Gson gson = new Gson();
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.smartSceneService.sceneActivate(apikey,timeStamp,sign, HttpConstant.API_VERSION,"scene/activate",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("e:"+e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        smartSceneControlPresenter.onSucess(responseBody);

                    }
                });
    }

    public interface sceneControlListener{
        void onSucess(ResponseBody responseBody);
        void onFailure();
    }
}
