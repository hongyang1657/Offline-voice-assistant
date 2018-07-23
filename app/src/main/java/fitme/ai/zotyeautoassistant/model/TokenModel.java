package fitme.ai.zotyeautoassistant.model;

import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.TokenInfo;
import fitme.ai.zotyeautoassistant.model.impl.ITokenModel;
import fitme.ai.zotyeautoassistant.presenter.TokenPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录获取用户信息的model
 * Created by blw on 2016/8/26.
 */
public class TokenModel implements ITokenModel {
    private static final String TAG = TokenModel.class.getSimpleName();
    //用于执行回调方法
    private TokenPresenter mTokenPresenter;

    public TokenModel(TokenPresenter mTokenPresenter) {
        this.mTokenPresenter = mTokenPresenter;
    }

    //登录请求
    @Override
    public void token(final String userId, String password) {
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.authorization_api_key;
        String apiSecret= ApiManager.authorization_api_secret;
        //MD5非对称加密
        final String asymmetric = SignAndEncrypt.asymmetricEncryptMd5(password,apiSecret, timeStamp);
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", HttpConstant.METHOD_AT_TOKEN);
        params.put("api_key", apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("password", asymmetric);
        Gson gson = new Gson();
        params.put("http_body", gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.AuthorizationService.token(apikey, timeStamp, sign,HttpConstant.API_VERSION, HttpConstant.METHOD_AT_TOKEN,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TokenInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mTokenPresenter.onFailure(e);
                    }
                    @Override
                    public void onNext(TokenInfo jsonObject) {
                        L.i("token onNext:"+new Gson().toJson(jsonObject));
                        if ("success".equals(jsonObject.getStatus())) {
                            //上传设备信息
                            SharedPreferencesUtils.getInstance().setStringKeyValue(SPConstants.SP_AYAH_USER_TOKEN, jsonObject.getToken());

                        }
                        mTokenPresenter.onSuccess(userId,jsonObject);
                    }
                });
    }

    public interface OnTokenListener {
        void onSuccess(String userId, TokenInfo jsonObject);

        void onFailure(Throwable e);
    }
}
