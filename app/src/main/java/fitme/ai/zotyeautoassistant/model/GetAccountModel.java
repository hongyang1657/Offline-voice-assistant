package fitme.ai.zotyeautoassistant.model;

import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.api.HttpConstant;
import fitme.ai.zotyeautoassistant.bean.AccountGet;
import fitme.ai.zotyeautoassistant.model.impl.IGetAccountModel;
import fitme.ai.zotyeautoassistant.presenter.GetAccountPersenter;
import fitme.ai.zotyeautoassistant.utils.SPConstants;
import fitme.ai.zotyeautoassistant.utils.SharedPreferencesUtils;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zzy on 2017/11/22.
 */

public class GetAccountModel implements IGetAccountModel {

    //用于执行回调方法
    private GetAccountPersenter mGetAccountPersenter;

    public GetAccountModel(GetAccountPersenter mGetAccountPersenter) {
        this.mGetAccountPersenter = mGetAccountPersenter;
    }

    /**
     *
     * @param brand_id  0 查询所有
     */
    @Override
    public void accountGet( long brand_id) {
        String userId= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USERID);
        String token= SharedPreferencesUtils.getInstance().getStringValueByKey(SPConstants.SP_AYAH_USER_TOKEN);
        String timeStamp = SignAndEncrypt.getTimeStamp();
        String apikey= ApiManager.notification_api_key;
        String apiSecret= ApiManager.notification_api_secret;
        HashMap<String,Object> params = new HashMap<>();
        params.put("method", HttpConstant.METHOD_SD_ACCOUNT_GET);
        params.put("api_key",apikey);
        params.put("timestamp", timeStamp);
        params.put("version", HttpConstant.API_VERSION);
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("token",token);
        map.put("brand_id",brand_id);
        Gson gson = new Gson();
        params.put("http_body",gson.toJson(map));
        String sign = SignAndEncrypt.signRequest(params, apiSecret);
        ApiManager.deviceControlService.accountGet(apikey,timeStamp,sign, HttpConstant.API_VERSION,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccountGet>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mGetAccountPersenter.onFailure(e);
                    }

                    @Override
                    public void onNext(AccountGet accountGet) {
                        mGetAccountPersenter.onSuccess(accountGet);
                    }
                });
    }

    public interface onAccountGetListener {
        void onSuccess(AccountGet accountGet);

        void onFailure(Throwable e);
    }
}
