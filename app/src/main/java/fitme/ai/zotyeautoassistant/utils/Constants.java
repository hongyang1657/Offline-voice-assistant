package fitme.ai.zotyeautoassistant.utils;

public class Constants {
    public static final String TAG = "debug_message";
    public static final String FITME_SERVICE_COMMUNICATION = "fitme.ai.zotye.service.communication";
    public static final String WAKE_UP_STATE = "wake_up_state";
    public static final String ASR_STATE = "asr_state";
    public static final String ASR_RESPONSE = "asr_response";
    public static final String ASR_VOLUME = "asr_volume";
    public static final String TTS_TEXT = "tts_text";
    public static final String TTS_CONTROL = "tts_control";
    public static final String TTS_STATE = "tts_state";
    public static final String LOG = "log";
    public static final String LOG_LOCAL = "log_local";
    public static final String LOGIN_STATE = "login_state";

    //唤醒状态
    public static final int WAKE_UP = 11;
    public static final int AWAIT_WAKE_UP = 12;

    //ASR状态
    public static final int ASR_STATE_DEFAULT = 20;   //默认状态
    public static final int ASR_STATE_RESPONSE_TIMEOUT = 21;   //识别超时
    public static final int ASR_STATE_SPEECH_START = 22;   //开始说话
    public static final int ASR_STATE_SPEECH_TIMEOUT = 23;   //说话超时
    public static final int ASR_STATE_SPEECH_END = 24;   //说话结束
    public static final int ASR_STATE_ERROR = 25;   //识别出错

    //tts控制
    public static final int TTS_START = 31;
    public static final int TTS_PAUSE = 32;
    public static final int TTS_RESUME = 33;
    public static final int TTS_STOP = 34;
    //tts状态
    public static final int TTS_PLAY_START = 35;
    public static final int TTS_PLAY_END = 36;
    public static final int TTS_UNKNOW = 37;


    //飞行控制intent指令对应的action
    public static final String START_FLYING_CONTROL_SYSTEM = "启动飞控软件";
    public static final String ENTER_AIR_LINE = "装订航线";
    public static final String TAKE_OFF = "一键起飞";
    public static final String INSTRUCT_FLIGHT = "指令飞行";
    public static final String TURN_LEFT = "左飞";
    public static final String TURN_RIGHT = "右飞";
    public static final String STRAIGHT_FLIGHT = "直飞";
    public static final String CLIMB = "爬升";
    public static final String LEVEL_FLIGHT = "平飞";
    public static final String DECLINE = "下降";
    public static final String CLOSE_ENGINE_CONSOLE = "关发动机页面";
    public static final String AUTO_FLIGHT = "自主飞行";
    public static final String CHECK_ENGINE = "查看发动机页面";
    public static final String RAISE_LANDING_GEAR = "收起落架";
    public static final String PUT_LANDING_GEAR = "放起落架";
    public static final String STOP_FLYING_CONTROL_SYSTEM = "关闭飞控软件";

    //没有intent，前端过滤
    public static final String OPEN_CONFIRMATION = "打开语音确认";
    public static final String CLOSE_CONFIRMATION = "关闭语音确认";

    public static final String CHAT = "回应聊天";
}
