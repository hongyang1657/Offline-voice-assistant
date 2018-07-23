package fitme.ai.zotyeautoassistant.model;

import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.presenter.MessageGetPresenter;
import fitme.ai.zotyeautoassistant.presenter.impl.IMessageGetPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.Mac;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hongy on 2017/12/19.
 */

public class MessageGetModel implements IMessageGetPresenter {

    private MessageGetPresenter messageGetPresenter;


    public MessageGetModel(MessageGetPresenter messageGetPresenter) {
        this.messageGetPresenter = messageGetPresenter;
    }

    @Override
    public void messageGet() {
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token =SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "message/to_user");
        params.put("api_key", ApiManager.notification_api_key);
        params.put("timestamp", timeStamp);
        params.put("version",ApiManager.VERSION);

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("device_id", Mac.getMac());
        map.put("token", token);
        map.put("max_count", 1);

        Gson gson = new Gson();
        params.put("http_body",gson.toJson(map));

        String sign = SignAndEncrypt.signRequest(params, ApiManager.notification_api_secret);
        ApiManager.NotificationService.getMessage(ApiManager.notification_api_key, timeStamp,sign,ApiManager.VERSION,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MessageGet>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("e:"+e.toString());
                    }

                    @Override
                    public void onNext(MessageGet messageGet) {
                        /*try {
                            L.i("ResponseBodymessageGet:"+messageGet.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        messageGetPresenter.onSucess(messageGet);
                    }
                });
    }

    public interface messageGetListener{
        void onSucess(MessageGet messageGet);
        void onFailure();
    }
}
