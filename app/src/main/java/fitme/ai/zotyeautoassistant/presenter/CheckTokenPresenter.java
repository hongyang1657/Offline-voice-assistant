package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.CheckToken;
import fitme.ai.zotyeautoassistant.model.CheckTokenModel;
import fitme.ai.zotyeautoassistant.presenter.impl.ICheckTokenPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;

/**
 * Created by blw on 2016/8/28.
 */
public class CheckTokenPresenter implements ICheckTokenPresenter, CheckTokenModel.OnCheckTokenListener {
    //网络数据处理model
    private CheckTokenModel mTokenModel;
    //视图fragment
    private ILoginFragmentView iLoginFragmentView;

    public CheckTokenPresenter(ILoginFragmentView iLoginFragmentView) {
        this.mTokenModel = new CheckTokenModel(this);
        this.iLoginFragmentView = iLoginFragmentView;
    }

    @Override
    public void onSuccess(String userId, CheckToken jsonObject) {
        iLoginFragmentView.checkToken(userId,jsonObject);
    }

    @Override
    public void onFailure(Throwable e) {

    }

    //登录网络请求
    @Override
    public void checkToken(String userId, String token) {
        mTokenModel.checkToken(userId, token);
    }
}
