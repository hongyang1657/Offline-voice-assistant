package fitme.ai.zotyeautoassistant.view.impl;

import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.bean.Status;
import okhttp3.ResponseBody;

public interface IMessageManageService {
    void getMessageResp(MessageGet messageGet);
    void getMessageArrivedResp(Status status);
    void getMessageCreatResp(Status status);
    void getSceneActivate(ResponseBody responseBody);
    void getDeviceConfig(DeviceConfigBean responseBody);
}
