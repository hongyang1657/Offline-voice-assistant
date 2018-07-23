package fitme.ai.zotyeautoassistant.presenter;

import android.content.Context;

import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.model.DeviceInfoUploadModel;
import fitme.ai.zotyeautoassistant.presenter.impl.IDeviceInfoUploadPresenter;
import fitme.ai.zotyeautoassistant.view.impl.ILoginFragmentView;


/**
 * Created by hongy on 2017/12/19.
 */

public class DeviceInfoUploadPresenter implements IDeviceInfoUploadPresenter,DeviceInfoUploadModel.deviceInfoUploadListener{

    private DeviceInfoUploadModel deviceInfoUploadModel;
    private ILoginFragmentView iLoginFragmentView;

    public DeviceInfoUploadPresenter(ILoginFragmentView iLoginFragmentView) {
        this.iLoginFragmentView = iLoginFragmentView;
        deviceInfoUploadModel = new DeviceInfoUploadModel(this);
    }

    @Override
    public void deviceInfoUpload(Context context) {
        deviceInfoUploadModel.deviceInfoUpload(context);
    }


    @Override
    public void onSucess(Status responseBody) {
        iLoginFragmentView.getDeviceInfoUpload(responseBody);
    }

    @Override
    public void onFailure() {

    }

}
