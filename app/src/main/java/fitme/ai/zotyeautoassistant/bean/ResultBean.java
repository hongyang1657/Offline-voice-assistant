package fitme.ai.zotyeautoassistant.bean;

import java.util.List;
import java.util.Map;

/**
 * 模型预测的结果返回bean
 *
 * Created by zzy on 2018/7/20.
 */

public class ResultBean {
    private String state;
    private String from_message_id;
    private String intent;
    private List<String> slot;
    private float intent_score;

    public ResultBean() {
    }

    public ResultBean(String state, String from_message_id, String intent, List<String> slot, float intent_score) {
        this.state = state;
        this.from_message_id = from_message_id;
        this.intent = intent;
        this.slot = slot;
        this.intent_score = intent_score;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFrom_message_id() {
        return from_message_id;
    }

    public void setFrom_message_id(String from_message_id) {
        this.from_message_id = from_message_id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<String> getSlot() {
        return slot;
    }

    public void setSlot(List<String> slot) {
        this.slot = slot;
    }

    public float getIntent_score() {
        return intent_score;
    }

    public void setIntent_score(float intent_score) {
        this.intent_score = intent_score;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "state='" + state + '\'' +
                ", from_message_id='" + from_message_id + '\'' +
                ", intent='" + intent + '\'' +
                ", slot=" + slot +
                ", intent_score=" + intent_score +
                '}';
    }
}
