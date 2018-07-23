package fitme.ai.zotyeautoassistant.bean;

/**
 * Created by hongy on 2018/4/3.
 */

public class DeviceConfigBean {
    private String status;
    private long modify_time;
    private DeviceConfig[] device_config;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getModify_time() {
        return modify_time;
    }

    public void setModify_time(long modify_time) {
        this.modify_time = modify_time;
    }

    public DeviceConfig[] getDevice_config() {
        return device_config;
    }

    public void setDevice_config(DeviceConfig[] device_config) {
        this.device_config = device_config;
    }

    public class DeviceConfig{
        private String device_id;
        private String device_type;
        private String device_name;
        private String room;
        private String sleep_model;
        private String start_time;
        private String end_time;
        private String system_version;
        private String wifi;
        private String serial_number;
        private String mac;
        private String ip;
        private int online_status;
        private UserConfig user_config;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getSleep_model() {
            return sleep_model;
        }

        public void setSleep_model(String sleep_model) {
            this.sleep_model = sleep_model;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getSystem_version() {
            return system_version;
        }

        public void setSystem_version(String system_version) {
            this.system_version = system_version;
        }

        public String getWifi() {
            return wifi;
        }

        public void setWifi(String wifi) {
            this.wifi = wifi;
        }

        public String getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(String serial_number) {
            this.serial_number = serial_number;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getOnline_status() {
            return online_status;
        }

        public void setOnline_status(int online_status) {
            this.online_status = online_status;
        }

        public UserConfig getUser_config() {
            return user_config;
        }

        public void setUser_config(UserConfig user_config) {
            this.user_config = user_config;
        }

        public class UserConfig{
            private Speechs[] speechs;

            public Speechs[] getSpeechs() {
                return speechs;
            }

            public void setSpeechs(Speechs[] speechs) {
                this.speechs = speechs;
            }


        }
    }

    public class Speechs{
        private String speech;
        private String as;
        private String before_speech;

        public String getSpeech() {
            return speech;
        }

        public void setSpeech(String speech) {
            this.speech = speech;
        }

        public String getAs() {
            return as;
        }

        public void setAs(String as) {
            this.as = as;
        }

        public String getBefore_speech() {
            return before_speech;
        }

        public void setBefore_speech(String before_speech) {
            this.before_speech = before_speech;
        }
    }
}
