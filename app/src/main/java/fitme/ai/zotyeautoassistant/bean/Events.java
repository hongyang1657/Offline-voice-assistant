package fitme.ai.zotyeautoassistant.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by zzy on 2018/7/17.
 */

public class Events {
    private String type; //事件类别，｛u2a_speech , u2a_api_call , a2u_speech , a2u_api_call , a2s，event2a｝,单轮只模拟u2a_speech
    private String speech; //言语，只有当 type == *_speech 时才有效
    private String intent; //意图，只有当 type != u2a_speech 时才有效
    private Map<String, Object> request_slots; //请求槽集合，只有当 type != u2a_speech 时才有效，属性名是槽名，属性值是槽值，槽值是string类型
    private Map<String, Object> response_slots; //应答槽集合，只有当 type == a2s 时才有效，属性名是槽名，属性值是槽值，槽值是string类型

    public Events() {
    }

    public Events(String type, String speech, String intent, Map<String, Object> request_slots, Map<String, Object> response_slots) {
        this.type = type;
        this.speech = speech;
        this.intent = intent;
        this.request_slots = request_slots;
        this.response_slots = response_slots;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Map<String, Object> getRequest_slots() {
        return request_slots;
    }

    public void setRequest_slots(Map<String, Object> request_slots) {
        this.request_slots = request_slots;
    }

    public Map<String, Object> getResponse_slots() {
        return response_slots;
    }

    public void setResponse_slots(Map<String, Object> response_slots) {
        this.response_slots = response_slots;
    }

    @Override
    public String toString() {
        return "Events{" +
                "type='" + type + '\'' +
                ", speech='" + speech + '\'' +
                ", intent='" + intent + '\'' +
                ", request_slots=" + request_slots +
                ", response_slots=" + response_slots +
                '}';
    }
}
