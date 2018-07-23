package fitme.ai.zotyeautoassistant.bean;

/**
 * Created by hongy on 2017/8/23.
 */

public class DeviceBean {
    private String identifier;
    private boolean is_bind;
    private String device_id;
    private String bind_user_id;
    private String did;
    private String nickname;
    private String pid;
    private String mac;
    private String brand;
    private String device_type_name;
    private boolean device_lock;
    private String device_type_number;
    private String user_group;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean is_bind() {
        return is_bind;
    }

    public void setIs_bind(boolean is_bind) {
        this.is_bind = is_bind;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getBind_user_id() {
        return bind_user_id;
    }

    public void setBind_user_id(String bind_user_id) {
        this.bind_user_id = bind_user_id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice_type_name() {
        return device_type_name;
    }

    public void setDevice_type_name(String device_type_name) {
        this.device_type_name = device_type_name;
    }

    public boolean isDevice_lock() {
        return device_lock;
    }

    public void setDevice_lock(boolean device_lock) {
        this.device_lock = device_lock;
    }

    public String getDevice_type_number() {
        return device_type_number;
    }

    public void setDevice_type_number(String device_type_number) {
        this.device_type_number = device_type_number;
    }

    public String getUser_group() {
        return user_group;
    }

    public void setUser_group(String user_group) {
        this.user_group = user_group;
    }
}
