package fitme.ai.zotyeautoassistant.model;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedHashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.presenter.MediaContinuePresenter;
import fitme.ai.zotyeautoassistant.presenter.impl.IMediaContinuePresenter;
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

public class MediaContinueModel implements IMediaContinuePresenter {
    private MediaContinuePresenter mediaContinuePresenter;

    public MediaContinueModel(MediaContinuePresenter mediaContinuePresenter) {
        this.mediaContinuePresenter = mediaContinuePresenter;
    }

    @Override
    public void mediaContinue() {
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token =SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String apikey= ApiManager.user_center_api_key;
        String apiSecret= ApiManager.user_center_api_secret;
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("method","player/resume");
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
        ApiManager.MediaPlayerService.mediaContinue(apikey,timeStamp,sign, HttpConstant.API_VERSION,"player/resume",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("e:"+e.toString());
                    }

                    @Override
                    public void onNext(Status responseBody) {
                        mediaContinuePresenter.onSucess(responseBody);

                    }
                });
    }

    public interface mediaContinueListener{
        void onSucess(Status responseBody);
        void onFailure();
    }
}
