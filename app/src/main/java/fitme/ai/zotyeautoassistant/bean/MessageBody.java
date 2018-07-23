package fitme.ai.zotyeautoassistant.bean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 消息实体类
 * Created by yml on 2017/10/18.
 */

public class MessageBody {

    private String speech; //言语
    private String icon_url;//图标url，可为空
    private String url;//url
    private String anchor;//锚文本，可为空
    private String skill_name;//技能名称
    private String h1;//h1
    private String h2;//h2
    private List<Rows> rows;//2-3行row
    private String image_url; //图片url
    private String center;//center
    private String sub_title;//此标题

    private String sdk;//要中转到的SDK。比如：博联
    private String intent;//意图
    //TODO 待处理 具体使用还未定义
    private Slots slots; //槽列表


    private String parent_task_id;
    private String task_result_id;
    private String task_id;
    private String message_id;
    private long task_result_time;
    private String task_type;
    private Map<String, Object> task_result_body;
    private String task_result_speech_text;
    private Recommendation[] recommendations;

    // message_type="speech"
    private String content;

    // message_type="pay_request"
    private String pay_request_id;//支付请求id
    private int amount;//订单总金额, 单位为分
    private String provider_name;//商户名称
    private String title;//订单的标题
    private String description;//订单的描述信息
    private int balance;//用户当前可用余额, 单位：分

    public MessageBody() {

    }
    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Slots getSlots() {
        return slots;
    }

    public void setSlots(Slots slots) {
        this.slots = slots;
    }

    public String getParent_task_id() {
        return parent_task_id;
    }

    public void setParent_task_id(String parent_task_id) {
        this.parent_task_id = parent_task_id;
    }

    public String getTask_result_id() {
        return task_result_id;
    }

    public void setTask_result_id(String task_result_id) {
        this.task_result_id = task_result_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public long getTask_result_time() {
        return task_result_time;
    }

    public void setTask_result_time(long task_result_time) {
        this.task_result_time = task_result_time;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Map<String, Object> getTask_result_body() {
        return task_result_body;
    }

    public void setTask_result_body(Map<String, Object> task_result_body) {
        this.task_result_body = task_result_body;
    }

    public String getTask_result_speech_text() {
        return task_result_speech_text;
    }

    public void setTask_result_speech_text(String task_result_speech_text) {
        this.task_result_speech_text = task_result_speech_text;
    }

    public Recommendation[] getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Recommendation[] recommendations) {
        this.recommendations = recommendations;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPay_request_id() {
        return pay_request_id;
    }

    public void setPay_request_id(String pay_request_id) {
        this.pay_request_id = pay_request_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "speech='" + speech + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", url='" + url + '\'' +
                ", anchor='" + anchor + '\'' +
                ", skill_name='" + skill_name + '\'' +
                ", h1='" + h1 + '\'' +
                ", h2='" + h2 + '\'' +
                ", rows=" + rows +
                ", image_url='" + image_url + '\'' +
                ", center='" + center + '\'' +
                ", sub_title='" + sub_title + '\'' +
                ", sdk='" + sdk + '\'' +
                ", intent='" + intent + '\'' +
                ", slots=" + slots +
                ", parent_task_id='" + parent_task_id + '\'' +
                ", task_result_id='" + task_result_id + '\'' +
                ", task_id='" + task_id + '\'' +
                ", message_id='" + message_id + '\'' +
                ", task_result_time=" + task_result_time +
                ", task_type='" + task_type + '\'' +
                ", task_result_body=" + task_result_body +
                ", task_result_speech_text='" + task_result_speech_text + '\'' +
                ", recommendations=" + Arrays.toString(recommendations) +
                ", content='" + content + '\'' +
                ", pay_request_id='" + pay_request_id + '\'' +
                ", amount=" + amount +
                ", provider_name='" + provider_name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", balance=" + balance +
                '}';
    }

}
