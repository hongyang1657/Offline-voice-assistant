package fitme.ai.zotyeautoassistant.bean;

/**
 * 槽值类
 * Created by zzy on 2017/11/24.
 */

public class Slots {
    private String device_id;//设备唯一id
    private String command_code;//控制码
    private String device_type;//设备类型
    private String module_type;//模块类型
    private String volume;

    //音乐
    private String url;
    private String 播放类别;
    private String 循环秒数;
    private String 缓存秒数;
    private String 位置;
    private String speech;
    private String 电梯状态;
    private String 温度值;
    private String 温度调高值;
    private String 温度调低值;
    private String 联系人名;
    private String 电话号码;
    private String 目的地;
    private String 音量值;

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    //位置

    public String get播放类别() {
        return 播放类别;
    }

    public void set播放类别(String 播放类别) {
        this.播放类别 = 播放类别;
    }

    public String get循环秒数() {
        return 循环秒数;
    }

    public void set循环秒数(String 循环秒数) {
        this.循环秒数 = 循环秒数;
    }

    public String get缓存秒数() {
        return 缓存秒数;
    }

    public void set缓存秒数(String 缓存秒数) {
        this.缓存秒数 = 缓存秒数;
    }

    public String get位置() {
        return 位置;
    }

    public void set位置(String 位置) {
        this.位置 = 位置;
    }

    //音量

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCommand_code() {
        return command_code;
    }

    public void setCommand_code(String command_code) {
        this.command_code = command_code;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getModule_type() {
        return module_type;
    }

    public void setModule_type(String module_type) {
        this.module_type = module_type;
    }

    public String get电梯状态() {
        return 电梯状态;
    }

    public void set电梯状态(String 电梯状态) {
        this.电梯状态 = 电梯状态;
    }

    public String get温度值() {
        return 温度值;
    }

    public void set温度值(String 温度值) {
        this.温度值 = 温度值;
    }

    public String get温度调高值() {
        return 温度调高值;
    }

    public void set温度调高值(String 温度调高值) {
        this.温度调高值 = 温度调高值;
    }

    public String get温度调低值() {
        return 温度调低值;
    }

    public void set温度调低值(String 温度调低值) {
        this.温度调低值 = 温度调低值;
    }

    public String get联系人名() {
        return 联系人名;
    }

    public void set联系人名(String 联系人名) {
        this.联系人名 = 联系人名;
    }

    public String get电话号码() {
        return 电话号码;
    }

    public void set电话号码(String 电话号码) {
        this.电话号码 = 电话号码;
    }

    public String get目的地() {
        return 目的地;
    }

    public void set目的地(String 目的地) {
        this.目的地 = 目的地;
    }

    public String get音量值() {
        return 音量值;
    }

    public void set音量值(String 音量值) {
        this.音量值 = 音量值;
    }

    @Override
    public String toString() {
        return "Slots{" +
                "device_id='" + device_id + '\'' +
                ", command_code='" + command_code + '\'' +
                ", device_type='" + device_type + '\'' +
                ", module_type='" + module_type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
