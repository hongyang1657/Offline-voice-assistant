package fitme.ai.zotyeautoassistant.bean;

import java.util.Map;

/**
 * recommendations推荐类型
 * Created by yml on 2017/10/18.
 */

public class Recommendation {
    private String task_title;
    private String task_type;
    private Map<String,Object> task_body;

    public Recommendation(String task_title, String task_type, Map<String, Object> task_body) {
        this.task_title = task_title;
        this.task_type = task_type;
        this.task_body = task_body;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Map<String, Object> getTask_body() {
        return task_body;
    }

    public void setTask_body(Map<String, Object> task_body) {
        this.task_body = task_body;
    }
}
