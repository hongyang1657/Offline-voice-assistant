package fitme.ai.zotyeautoassistant.view.impl;

import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.bean.Status;

public interface IMessageManageService {
    void getMessageResp(MessageGet messageGet);
    void getMessageArrivedResp(Status status);
    void getMessageCreatResp(Status status);
}
