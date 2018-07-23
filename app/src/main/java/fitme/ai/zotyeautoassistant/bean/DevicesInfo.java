package fitme.ai.zotyeautoassistant.bean;

import java.util.List;

/**
 * Created by hongy on 2018/3/15.
 */

public class DevicesInfo {
    private String status;
    private List<Devices> devices;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Devices> getDevices() {
        return devices;
    }

    public void setDevices(List<Devices> devices) {
        this.devices = devices;
    }
}
