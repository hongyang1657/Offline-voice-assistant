package fitme.ai.zotyeautoassistant.presenter;

import android.content.Context;


import fitme.ai.zotyeautoassistant.model.DeviceConfigPutModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IDeviceConfigPutPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;
import okhttp3.ResponseBody;

/**
 * Created by hongy on 2017/12/19.
 */

public class DeviceConfigPutPresenter implements IDeviceConfigPutPresenter,DeviceConfigPutModel.deviceConfigPutListener{

    private DeviceConfigPutModel deviceConfigPutModel;
    private ILoginFragmentView iLoginFragmentView;

    public DeviceConfigPutPresenter(ILoginFragmentView iLoginFragmentView) {
        this.iLoginFragmentView = iLoginFragmentView;
        deviceConfigPutModel = new DeviceConfigPutModel(this);
    }

    @Override
    public void deviceConfigPut(Context context, String path, Object value) {
        deviceConfigPutModel.deviceConfigPut(context,path,value);
    }


    @Override
    public void onSucess(ResponseBody responseBody) {
        iLoginFragmentView.getDeviceConfigPut(responseBody);
    }

    @Override
    public void onFailure() {

    }

}
