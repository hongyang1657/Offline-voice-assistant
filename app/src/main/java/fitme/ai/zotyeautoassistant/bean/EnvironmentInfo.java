package fitme.ai.zotyeautoassistant.bean;

/**
 * Created by zzy on 2018/1/5.
 */

public class EnvironmentInfo {
    private String air; //空气质量
    private String humidity; //湿度
    private String light; //光线
    private String noisy; //声音
    private String temperature;//温度

    public EnvironmentInfo() {

    }

    public EnvironmentInfo(String air, String humidity, String light, String noisy, String temperature) {
        this.air = air;
        this.humidity = humidity;
        this.light = light;
        this.noisy = noisy;
        this.temperature = temperature;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getNoisy() {
        return noisy;
    }

    public void setNoisy(String noisy) {
        this.noisy = noisy;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "EnvironmentInfo{" +
                "air='" + air + '\'' +
                ", humidity='" + humidity + '\'' +
                ", light='" + light + '\'' +
                ", noisy='" + noisy + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}
