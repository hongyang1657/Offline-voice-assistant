package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.AccountGet;
import fitme.ai.zotyeautoassistant.model.GetAccountModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IGetAccountPersenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;

/**
 * Created by zzy on 2017/11/22.
 */

public class GetAccountPersenter implements IGetAccountPersenter, GetAccountModel.onAccountGetListener {

    //网络处理
    private GetAccountModel mGetAccountModel;
    //视图处理
    private ILoginFragmentView iLoginFragmentView;

    public GetAccountPersenter(ILoginFragmentView iLoginFragmentView) {
        this.mGetAccountModel = new GetAccountModel(this);
        this.iLoginFragmentView = iLoginFragmentView;
    }

    @Override
    public void accountGet(long brand_id) {
        mGetAccountModel.accountGet(brand_id);
    }

    @Override
    public void onSuccess(AccountGet accountGet) {
        iLoginFragmentView.showAccountGet(accountGet);
    }

    @Override
    public void onFailure(Throwable e) {

    }
}
