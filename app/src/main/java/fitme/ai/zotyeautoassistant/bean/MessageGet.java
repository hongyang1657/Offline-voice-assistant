package fitme.ai.zotyeautoassistant.bean;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by blw on 2016/9/12.
 */
public class MessageGet {
    private String status;
    private Messages[] messages;

    public MessageGet() {
    }

    public MessageGet(String status, Messages[] messages) {
        this.status = status;
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MessageGet{" +
                "status='" + status + '\'' +
                ", messages=" + Arrays.toString(messages) +
                ",messages[0]="+messages[0].toString()+
                '}';
    }

    public Messages[] getMessages() {
        return messages;
    }

    public void setMessages(Messages[] messages) {
        this.messages = messages;
    }
    /**
     * recommendations推荐类型
     */
    public class Recommendation{
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

}
