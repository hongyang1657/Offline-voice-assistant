package fitme.ai.zotyeautoassistant.presenter;

import android.content.Context;

import fitme.ai.zotyeautoassistant.bean.DevicesInfo;
import fitme.ai.zotyeautoassistant.model.DeviceSearchModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IDeviceSearchPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;


/**
 * Created by hongy on 2017/12/19.
 */

public class DeviceSearchPresenter implements IDeviceSearchPresenter,DeviceSearchModel.deviceSearchListener{

    private DeviceSearchModel deviceSearchModel;
    private ILoginFragmentView iLoginFragmentView;

    public DeviceSearchPresenter(ILoginFragmentView iLoginFragmentView) {
        this.iLoginFragmentView = iLoginFragmentView;
        deviceSearchModel = new DeviceSearchModel(this);
    }

    @Override
    public void deviceSearch(Context context) {
        deviceSearchModel.deviceSearch(context);
    }


    @Override
    public void onSucess(DevicesInfo responseBody) {
        iLoginFragmentView.getDeviceSearchRes(responseBody);
    }

    @Override
    public void onFailure() {

    }

}
