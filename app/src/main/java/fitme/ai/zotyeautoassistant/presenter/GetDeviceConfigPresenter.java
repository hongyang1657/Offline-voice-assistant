package fitme.ai.zotyeautoassistant.presenter;


import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.model.GetDeviceConfigModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IGetDeviceConfigPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;
import fitme.ai.zotyeautoassistant.view.impl.IMessageManageService;

/**
 * Created by hongy on 2017/12/22.
 */

public class GetDeviceConfigPresenter implements IGetDeviceConfigPresenter,GetDeviceConfigModel.getDeviceConfigListener{

    private GetDeviceConfigModel getDeviceConfigModel;
    private IMessageManageService iMessageManageService;

    public GetDeviceConfigPresenter(IMessageManageService iMessageManageService) {
        this.iMessageManageService = iMessageManageService;
        getDeviceConfigModel = new GetDeviceConfigModel(this);
    }

    @Override
    public void getDeviceConfig() {
        getDeviceConfigModel.getDeviceConfig();
    }

    @Override
    public void onSucess(DeviceConfigBean deviceConfigBean) {
        iMessageManageService.getDeviceConfig(deviceConfigBean);
    }


    @Override
    public void onFailure() {

    }

}
