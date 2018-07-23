package fitme.ai.zotyeautoassistant.presenter;

import android.content.Context;

import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.DeviceBindModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IDeviceBindPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;


/**
 * Created by hongy on 2017/12/19.
 */

public class DeviceBindPresenter implements IDeviceBindPresenter,DeviceBindModel.deviceBindListener{

    private DeviceBindModel deviceBindModel;
    private ILoginFragmentView iLoginFragmentView;

    public DeviceBindPresenter(ILoginFragmentView iLoginFragmentView) {
        this.iLoginFragmentView = iLoginFragmentView;
        deviceBindModel = new DeviceBindModel(this);
    }

    @Override
    public void deviceBind(Context context) {
        deviceBindModel.deviceBind(context);
    }


    @Override
    public void onSucess(Status responseBody) {
        iLoginFragmentView.getDeviceBindRes(responseBody);
    }

    @Override
    public void onFailure() {

    }

}
