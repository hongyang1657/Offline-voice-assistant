package fitme.ai.zotyeautoassistant.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.MyApplication;
import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.presenter.MessageCreatPresenter;
import fitme.ai.zotyeautoassistant.presenter.impl.IMessageCreatPresenter;
import fitme.ai.zotyeautoassistant.utils.CreateMessageIdUtils;
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

public class MessageCreatModel implements IMessageCreatPresenter {

    private MessageCreatPresenter messageCreatPresenter;

    public MessageCreatModel(MessageCreatPresenter messageCreatPresenter) {
        this.messageCreatPresenter = messageCreatPresenter;
    }

    @Override
    public void messageCreat(String speech, Context context) {
        String userId = SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token =SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        int longtitude = SharedPreferencesUtils.getInstance().getIntValueByKey(SPConstants.SP_AYAH_LONGTITUDE);
        int latitude = SharedPreferencesUtils.getInstance().getIntValueByKey(SPConstants.SP_AYAH_LATITUDE);
        String messageId = CreateMessageIdUtils.getMessageId(userId,context);
        //本地存messageId
        MyApplication.getInstance().getMessageId().add(0,messageId);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "message/from_user/create");
        params.put("api_key", ApiManager.dialog_manage_api_key);
        params.put("timestamp", timeStamp);
        params.put("version",ApiManager.VERSION);

        HashMap<String, Object> map = new HashMap<>();
        map.put("message_id", messageId);
        L.i("发送的messageID:"+messageId);
        map.put("user_id", userId);
        map.put("token", token);
        map.put("device_id", Mac.getMac());
        map.put("x", longtitude);
        map.put("y", latitude);
        map.put("speech", speech);
        map.put("member_id", "");
        map.put("intent", "");
        map.put("slots", null);

        Gson gson = new Gson();
        params.put("http_body",gson.toJson(map));
        L.i("http_bodyhttp_bodyhttp_bodyhttp_bodyhttp_body:"+gson.toJson(map));

        String sign = SignAndEncrypt.signRequest(params, ApiManager.dialog_manage_api_secret);
        ApiManager.DialogManagerService.messageCreate(ApiManager.dialog_manage_api_key, timeStamp,sign,ApiManager.VERSION,map)
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
                        messageCreatPresenter.onSucess(status);
                    }
                });
    }

    public interface messageCreatListener{
        void onSucess(Status status);
        void onFailure();
    }
}
