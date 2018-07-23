package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.model.GetDeviceConfigModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IGetDeviceConfigPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;

/**
 * Created by hongy on 2017/12/22.
 */

public class GetDeviceConfigPresenter implements IGetDeviceConfigPresenter,GetDeviceConfigModel.getDeviceConfigListener{

    private GetDeviceConfigModel getDeviceConfigModel;
    private ILoginFragmentView iLoginFragmentView;

    public GetDeviceConfigPresenter(ILoginFragmentView iLoginFragmentView) {
        this.iLoginFragmentView = iLoginFragmentView;
        getDeviceConfigModel = new GetDeviceConfigModel(this);
    }

    @Override
    public void getDeviceConfig() {
        getDeviceConfigModel.getDeviceConfig();
    }

    @Override
    public void onSucess(DeviceConfigBean deviceConfigBean) {
        iLoginFragmentView.getDeviceConfig(deviceConfigBean);
    }


    @Override
    public void onFailure() {

    }

}
