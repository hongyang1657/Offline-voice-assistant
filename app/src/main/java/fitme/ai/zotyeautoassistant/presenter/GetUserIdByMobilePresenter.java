package fitme.ai.zotyeautoassistant.presenter;

import fitme.ai.zotyeautoassistant.bean.AccountCreate;
import fitme.ai.zotyeautoassistant.model.GetUserIdByMobileModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IGetUserIdByMobilePresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;

/**
 * Created by yml on 2017/10/12.
 */

public class GetUserIdByMobilePresenter implements IGetUserIdByMobilePresenter, GetUserIdByMobileModel.GetUserIdByMobileListener {

    private ILoginFragmentView mILoginFragmentView;
    private GetUserIdByMobileModel mGetUserIdByMobileModel;

    public GetUserIdByMobilePresenter(ILoginFragmentView mILoginFragmentView) {
        this.mILoginFragmentView = mILoginFragmentView;
        this.mGetUserIdByMobileModel = new GetUserIdByMobileModel(this);
    }

    @Override
    public void getUserIdByMobile(String phoneNumber) {
        mGetUserIdByMobileModel.getUserIdByMobile(phoneNumber);
    }

    @Override
    public void onSuccess(AccountCreate user) {
        mILoginFragmentView.showGetUserId(user);
    }

    @Override
    public void onFailure(Throwable e) {
        mILoginFragmentView.failedToConnect(e);
    }
}
