package fitme.ai.zotyeautoassistant.presenter;

import android.content.Context;

import com.google.gson.Gson;


import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.DeviceBindSuccessModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IDeviceBindSuccessPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;


/**
 * Created by hongy on 2017/12/19.
 */

public class DeviceBindSuccessPresenter implements IDeviceBindSuccessPresenter,DeviceBindSuccessModel.deviceBindSuccessListener{

    private DeviceBindSuccessModel deviceBindSuccessModel;
    private ILoginFragmentView iLoginFragmentView;

    public DeviceBindSuccessPresenter(ILoginFragmentView iLoginFragmentView) {
        this.iLoginFragmentView = iLoginFragmentView;
        deviceBindSuccessModel = new DeviceBindSuccessModel(this);
    }

    @Override
    public void deviceBindSuccess(Context context) {
        deviceBindSuccessModel.deviceBindSuccess(context);
    }


    @Override
    public void onSucess(Status responseBody) {
        iLoginFragmentView.getDeviceBindSuccessRes(responseBody);
    }

    @Override
    public void onFailure() {

    }

}
