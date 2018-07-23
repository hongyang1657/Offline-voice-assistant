package fitme.ai.zotyeautoassistant.model;


import com.google.gson.Gson;

import java.util.HashMap;

import fitme.ai.zotyeautoassistant.api.ApiManager;
import fitme.ai.zotyeautoassistant.bean.AccountCreate;
import fitme.ai.zotyeautoassistant.model.impl.IGetUserIdByMobileModel;
import fitme.ai.zotyeautoassistant.presenter.GetUserIdByMobilePresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.SignAndEncrypt;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 实现请求根据手机号查询 user_id
 * Created by yml on 2017/10/12.
 */

public class GetUserIdByMobileModel implements IGetUserIdByMobileModel {

    private GetUserIdByMobilePresenter mGetUserIdByMobilePresenter;

    public GetUserIdByMobileModel(GetUserIdByMobilePresenter mGetUserIdByMobilePresenter) {
        this.mGetUserIdByMobilePresenter = mGetUserIdByMobilePresenter;
    }

    @Override
    public void getUserIdByMobile(String phoneNumber) {
        String timeStamp = SignAndEncrypt.getTimeStamp();
        HashMap<String, Object> params = new HashMap<>();
        params.put("method", "get_user_id_by_mobile");
        params.put("api_key", ApiManager.user_center_api_key);
        params.put("timestamp", timeStamp);
        params.put("version", ApiManager.VERSION);

        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", phoneNumber);
        Gson gson = new Gson();
        params.put("http_body",gson.toJson(map));

        String sign = SignAndEncrypt.signRequest(params, ApiManager.user_center_api_secret);
        ApiManager.UserCenterService.getUserIdByMobile(ApiManager.user_center_api_key, timeStamp,sign,ApiManager.VERSION,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccountCreate>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("================getUserIdByMobilee"+e.toString());
                        mGetUserIdByMobilePresenter.onFailure(e);
                    }

                    @Override
                    public void onNext(AccountCreate responseBody) {
                        L.i("================getUserIdByMobile=======onNext====================");
                        mGetUserIdByMobilePresenter.onSuccess(responseBody);
                    }
                });
    }

    public interface GetUserIdByMobileListener {
        void onSuccess(AccountCreate jsonObject);

        void onFailure(Throwable e);
    }

}
