package fitme.ai.zotyeautoassistant.model;


import com.google.gson.Gson;

import java.util.HashMap;


import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.presenter.MessageArrivedPresenter;
import fitme.ai.zotyeautoassistant.presenter.impl.IMessageArrivedPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by hongy on 2017/12/19.
 */

public class MessageArrivedModel implements IMessageArrivedPresenter {

    private MessageArrivedPresenter messageArrivedPresenter;

    public MessageArrivedModel(MessageArrivedPresenter messageArrivedPresenter) {
        this.messageArrivedPresenter = messageArrivedPresenter;
    }

    @Override
    public void messageArrived(String messageId) {
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token =SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "message_arrived/create");
        params.put("api_key", ApiManager.notification_api_key);
        params.put("timestamp", timeStamp);
        params.put("version", ApiManager.VERSION);

        HashMap<String, Object> map = new HashMap<>();
        map.put("message_id", messageId);
        map.put("user_id", userId);
        map.put("token", token);

        Gson gson = new Gson();
        params.put("http_body",gson.toJson(map));

        String sign = SignAndEncrypt.signRequest(params, ApiManager.notification_api_secret);
        ApiManager.NotificationService.messageArrived(ApiManager.notification_api_key, timeStamp,sign,ApiManager.VERSION,map)
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
                    public void onNext(Status status) {
                        L.i("消息到达确认成功"+new Gson().toJson(status));
                        messageArrivedPresenter.onSucess(status);
                    }
                });
    }


    public interface messageArrivedListener{
        void onSucess(Status status);
        void onFailure();
    }
}
