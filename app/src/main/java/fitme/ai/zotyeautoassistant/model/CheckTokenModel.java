package fitme.ai.zotyeautoassistant.model;

import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.CheckToken;
import fitme.ai.zotyeautoassistant.model.impl.ICheckTokenModel;
import fitme.ai.zotyeautoassistant.presenter.CheckTokenPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录获取用户信息的model
 * Created by blw on 2016/8/26.
 */
public class CheckTokenModel implements ICheckTokenModel {
    private static final String TAG = CheckTokenModel.class.getSimpleName();
    //用于执行回调方法
    private CheckTokenPresenter mTokenPresenter;

    public CheckTokenModel(CheckTokenPresenter mTokenPresenter) {
        this.mTokenPresenter = mTokenPresenter;
    }

    //登录请求
    @Override
    public void checkToken(final String userId, String token) {
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.authorization_api_key;
        String apiSecret= ApiManager.authorization_api_secret;
        //MD5非对称加密
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "check_token");
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("token", token);
        Gson gson = new Gson();
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.AuthorizationService.checkToken(apikey, timeStamp, sign,HttpConstant.API_VERSION, "check_token",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckToken>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("onError  token:"+e.toString());
                        mTokenPresenter.onFailure(e);
                    }
                    @Override
                    public void onNext(CheckToken jsonObject) {
                        mTokenPresenter.onSuccess(userId,jsonObject);
                    }
                });
    }

    public interface OnCheckTokenListener {
        void onSuccess(String userId, CheckToken jsonObject);

        void onFailure(Throwable e);
    }
}
