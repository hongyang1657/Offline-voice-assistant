package fitme.ai.zotyeautoassistant.view.impl;

import fitme.ai.zotyeautoassistant.bean.AccountCreate;
import fitme.ai.zotyeautoassistant.bean.AccountGet;
import fitme.ai.zotyeautoassistant.bean.CheckToken;
import fitme.ai.zotyeautoassistant.bean.DeviceConfigBean;
import fitme.ai.zotyeautoassistant.bean.DevicesInfo;
import fitme.ai.zotyeautoassistant.bean.MessageGet;
import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.bean.TokenInfo;
import okhttp3.ResponseBody;

/**
 * Created by yml on 2017/12/6.
 */

public interface ILoginFragmentView {
    /**
     * 根据mobile查询userId处理接口
     * @param user
     */
    void showGetUserId(AccountCreate user);

    void showToken(String userId, TokenInfo jsonObject);
    /**
     * 获取绑定结果
     * @param
     */
    void getDeviceBindRes(Status status);

    void getDeviceBindSuccessRes(Status responseBody);

    //获取上传硬件信息结果成功
    void getDeviceInfoUpload(Status status);

    //接口调用失败
    void failedToConnect(Throwable e);

    //查询交互设备
    void getDeviceSearchRes(DevicesInfo devicesInfo);

    void getDeviceConfigPut(ResponseBody responseBody);

    void showTokenFailed(Throwable e);

    void getDeviceConfig(DeviceConfigBean responseBody);


    void checkToken(String userId, CheckToken jsonObject);

    void showAccountGet(AccountGet accountGet);
}
