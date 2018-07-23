package fitme.ai.zotyeautoassistant.model;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedHashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.StatusWithUrl;
import fitme.ai.zotyeautoassistant.presenter.MediaNextPresenter;
import fitme.ai.zotyeautoassistant.presenter.impl.IMediaNextPresenter;
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

public class MediaNextModel implements IMediaNextPresenter {

    private MediaNextPresenter mediaNextPresenter;

    public MediaNextModel(MediaNextPresenter mediaNextPresenter) {
        this.mediaNextPresenter = mediaNextPresenter;
    }

    @Override
    public void mediaNext() {
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token =SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.user_center_api_key;
        String apiSecret= ApiManager.user_center_api_secret;
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("method","player/next");
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("device_id", Mac.getMac());
        map.put("type",0);
        map.put("token", token);
        Gson gson = new Gson();
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.MediaPlayerService.mediaNext(apikey,timeStamp,sign, HttpConstant.API_VERSION,"player/next",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StatusWithUrl>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("mediaNext_e:"+e.toString());
                    }

                    @Override
                    public void onNext(StatusWithUrl responseBody) {
                        mediaNextPresenter.onSucess(responseBody);

                    }
                });
    }

    public interface mediaNextListener{
        void onSucess(StatusWithUrl responseBody);
        void onFailure();
    }
}
