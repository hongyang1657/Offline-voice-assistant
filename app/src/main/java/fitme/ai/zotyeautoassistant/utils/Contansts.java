package fitme.ai.zotyeautoassistant.utils;

public class Contansts {
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


}
