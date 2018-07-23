package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.TokenInfo;
import fitme.ai.zotyeautoassistant.model.TokenModel;
import fitme.ai.zotyeautoassistant.presenter.impl.ITokenPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;

/**
 * Created by blw on 2016/8/28.
 */
public class TokenPresenter implements ITokenPresenter, TokenModel.OnTokenListener {
    //网络数据处理model
    private TokenModel mTokenModel;
    //视图fragment
    private ILoginFragmentView iLoginFragmentView;

    public TokenPresenter(ILoginFragmentView iLoginFragmentView) {
        this.mTokenModel = new TokenModel(this);
        this.iLoginFragmentView = iLoginFragmentView;
    }

    @Override
    public void onSuccess(String userId, TokenInfo jsonObject) {
        iLoginFragmentView.showToken(userId,jsonObject);
    }

    @Override
    public void onFailure(Throwable e) {
        iLoginFragmentView.showTokenFailed(e);
    }

    //登录网络请求
    @Override
    public void token(String userId, String password) {
        mTokenModel.token(userId, password);
    }
}
