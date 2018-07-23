package fitme.ai.zotyeautoassistant.bean;

import java.util.List;

/**
 * Created by zzy on 2018/7/19.
 */

public class SlotReturnBean {
    private List<Integer> x_passage;
    private List<Integer> x_intent;
    private List<Integer> x_slot_name;
    private List<String> new_chars;

    public SlotReturnBean() {

    }

    public SlotReturnBean(List<Integer> x_passage, List<Integer> x_intent, List<Integer> x_slot_name, List<String> new_chars) {
        this.x_passage = x_passage;
        this.x_intent = x_intent;
        this.x_slot_name = x_slot_name;
        this.new_chars = new_chars;
    }

    public List<Integer> getX_passage() {
        return x_passage;
    }

    public void setX_passage(List<Integer> x_passage) {
        this.x_passage = x_passage;
    }

    public List<Integer> getX_intent() {
        return x_intent;
    }

    public void setX_intent(List<Integer> x_intent) {
        this.x_intent = x_intent;
    }

    public List<Integer> getX_slot_name() {
        return x_slot_name;
    }

    public void setX_slot_name(List<Integer> x_slot_name) {
        this.x_slot_name = x_slot_name;
    }

    public List<String> getNew_chars() {
        return new_chars;
    }

    public void setNew_chars(List<String> new_chars) {
        this.new_chars = new_chars;
    }

    @Override
    public String toString() {
        return "SlotReturnBean{" +
                "x_passage=" + x_passage +
                ", x_intent=" + x_intent +
                ", x_slot_name=" + x_slot_name +
                ", new_chars=" + new_chars +
                '}';
    }
}
