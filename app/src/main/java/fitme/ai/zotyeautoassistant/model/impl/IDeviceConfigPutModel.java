package fitme.ai.zotyeautoassistant.model.impl;

import android.content.Context;

/**
 * 上传设备信息
 */

public interface IDeviceConfigPutModel {
    void deviceConfigPut(Context context, String path, Object value);
}
