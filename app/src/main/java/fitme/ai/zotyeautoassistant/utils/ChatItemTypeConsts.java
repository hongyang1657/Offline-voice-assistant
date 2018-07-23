package fitme.ai.zotyeautoassistant.utils;

/**
 * 对话流界面卡片的类型
 * Created by yml on 2017/11/6.
 */

public class ChatItemTypeConsts {

    //标准言语,聊天类型
    public static final String SPEECH_TYPE_STANDARD="speech/standard";
    //提醒，可跳转二级
    public static final String SPEECH_TYPE_URL="speech/url";
    //带一条提醒内容的卡片
    public static final String SPEECH_TYPE_URL_TITLE_H1_H2="speech/url_title_h1_h2";
    //带最多3条提醒内容的卡片
    public static final String SPEECH_TYPE_URL_ROWS_H1_H2="speech/url_rows_h1_h2";
    //带一张图片的一条信息的卡片，根据技能名称skill_name 再做区分   如:酒店
    public static final String SPEECH_TYPE_URL_TITLE_H1_IMG="speech/url_title_h1_img";
    //最多3条带图片信息的卡片，如查询酒店显示3条
    public static final String SPEECH_TYPE_URL_ROWS_TITLE_H1_IMG="speech/url_rows_title_h1_img";
    //没有图片，带标题 如：诊断
    public static final String SPEECH_TYPE_URL_TITLE_H1="speech/url_title_h1";
    //如：打车
    public static final String SPEECH_TYPE_URL_H1_H2_CENTER="speech/url_h1_h2_center";
    //如：播放音乐
    public static final String SPEECH_TYPE_URL_IMG_TITLE_H1="speech/url_img_title_h1";
    //SDK中继控制指令
    public static final String SPEECH_TYPE_RELAY_COMMAND="relay_command";
    ///API调用动作
    public static final String SPEECH_TYPE_API_CALL_ACTION="api_call_action";
    //auto_u2a_speech
    public static final String SPEECH_TYPE_AUTO_U2A_SPEECH="auto_u2a_speech";
}
