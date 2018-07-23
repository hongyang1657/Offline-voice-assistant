package fitme.ai.zotyeautoassistant.bean;

/**
 * 消息列表
 * Created by yml on 2017/10/18.
 */

public class Messages {
    private String message_id;//发给用户的消息Id
    private String from_message_id;//关联的来自用户的消息Id
    private String create_time;//(消息)创建时间，Unixtime
    private String message_type;//1.speech:文本; 2.pay_request:支付请求; 3.freeze:冻结结果; 4.charge:充值结果，5.auto_u2a_speech
    private MessageBody message_body;//消息体，内容结构随 message_type 变动
    private String user_id;//用户id
    private String device_id;//    推送到的设备id
    private String expires;//消息过期时间，UnixTime，消息过期后，APP不会再收到
    private String chatmessage;//用户或者系统说的话
    private String device_type;//区分来自音箱还是APP 的话

    public Messages() {
    }

    public Messages(String message_id, String time, String message_type, MessageBody message_body) {
        this.message_id = message_id;
        this.create_time = time;
        this.message_type = message_type;
        this.message_body = message_body;
    }

    public String getFrom_message_id() {
        return from_message_id;
    }

    public void setFrom_message_id(String from_message_id) {
        this.from_message_id = from_message_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getChatmessage() {
        return chatmessage;
    }

    public void setChatmessage(String chatmessage) {
        this.chatmessage = chatmessage;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public MessageBody getMessage_body() {
        return message_body;
    }

    public void setMessage_body(MessageBody message_body) {
        this.message_body = message_body;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "message_id='" + message_id + '\'' +
                ", from_message_id='" + from_message_id + '\'' +
                ", create_time=" + create_time +
                ", message_type='" + message_type + '\'' +
                ", message_body=" + message_body +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Messages)){
            return false;
        }
        Messages obj=(Messages)o;
        if(this.message_id.equals(obj.getMessage_id())){
            return true;
        }else{
            return false;
        }
    }

}
