package com.iflytek.speech;

import com.iflytek.speech.sr.ISRListener;

public class libisssr {
	private static final String tag = "libisssr";
	
	static {
		System.loadLibrary("ichip-jni");
		System.loadLibrary("iFlyPResBuild");
		System.loadLibrary("iFlyNLI");
		System.loadLibrary("msc");
		System.loadLibrary("cataIndex");
		System.loadLibrary("cata");

		System.loadLibrary("issauth");
		System.loadLibrary("SpWord");
		System.loadLibrary("kernel");
		System.loadLibrary("w_ivw");
		System.loadLibrary("w_ivwgram");
		System.loadLibrary("namext");
		System.loadLibrary("isr-jni");
	}

	//language
	final public static int ISS_SR_ACOUS_LANG_VALUE_MANDARIN = 0; //mandarin
	final public static int ISS_SR_ACOUS_LANG_VALUE_ENGLISH = 1; //english
	final public static int ISS_SR_ACOUS_LANG_VALUE_CANTONESE = 2; //cantonese

	//sr scene
	final public static String ISS_SR_SCENE_ALL = "all";                    ///< All scene
	final public static String ISS_SR_SCENE_POI = "poi";                    ///< POI navigation scene
	final public static String ISS_SR_SCENE_CONTACTS = "contacts";                ///< Contacts scene
	final public static String ISS_SR_SCENE_SELECT = "select";                ///< Select scene
	final public static String ISS_SR_SCENE_CONFIRM = "confirm";                ///< Confirm scene
	final public static String ISS_SR_SCENE_ANSWER_CALL = "answer_call";            ///< The scene of answering a call
	final public static String ISS_SR_SCENE_CMDLIST_WITHALL = "cmdlist_withall";        ///< Abandon

	final public static String ISS_SR_SCENE_STKS = "stks";                    ///< short time keyword select
	final public static String ISS_SR_SCENE_ONESHOT = "oneshot";                ///< OneShot scene

	final public static String ISS_SR_SCENE_SELECTLIST_POI = "selectlist_poi";        ///< only win32 and android, manadarin support
	final public static String ISS_SR_SCENE_SELECTLIST_CONTACTS = "selectlist_contacts";    ///< not support yet

	final public static String ISS_SR_SCENE_MUSIC = "music";                ///< Misic scene
	final public static String ISS_SR_SCENE_HIMALAYAFM = "himalayaFM";        ///< HiMaLaYa scene

	final public static String ISS_SR_SCENE_SELECT_MVW = "select_mvw";     ///For the select scene of the multiple awakening realizations
	final public static String ISS_SR_SCENE_CONFIRM_MVW = "confirm_mvw";   ///For the confirm scene of the multiple awakening realizations
	final public static String ISS_SR_SCENE_ANSWER_CALL_MVW  = "answer_call_mvw"; ///For the answering calls scene of the multiple awakening realizations
	final public static String ISS_SR_SCENE_BUILD_GRM_MVW  = "build_grm_mvw";    ///For the multi-scenario scene of the multiple awakening realizations
	//sr mode
	final public static int ISS_SR_MODE_CLOUD_REC = 0;              ///< Pure network recognition
	final public static int ISS_SR_MODE_LOCAL_REC = 1;              ///< Pure local recognition
	final public static int ISS_SR_MODE_MIX_REC = 2;              ///< Cloud and terminal mixed recognition
	final public static int ISS_SR_MODE_LOCAL_CMDLIST = 3;              ///< Pure local command word (abandoned)
	final public static int ISS_SR_MODE_LOCAL_NLP = 4;              ///< Pure local semantics
	final public static int ISS_SR_MODE_LOCAL_MVW = 5;              ///< Abandon

	//sr parameter , parameter value
	final public static String ISS_SR_PARAM_IAT_EXTEND_PARAMS = "iatextendparams";
	final public static String ISS_SR_PARAM_SPEECH_TIMEOUT = "speechtimeout";
	final public static String ISS_SR_PARAM_RESPONSE_TIMEOUT = "responsetimeout";
	final public static String ISS_SR_PARAM_SPEECH_TAIL = "speechtail";
	final public static String ISS_SR_PARAM_MVW_TIMEOUT = "mvwtimeout";
	final public static String ISS_SR_MVW_PARAM_AEC = "mvw_enable_aec";
	final public static String ISS_SR_MVW_PARAM_LSA = "mvw_enable_lsa";

	final public static String ISS_SR_PARAM_LONGTITUDE = "longitude";
	final public static String ISS_SR_PARAM_LATITUDE = "latitude";
	final public static String ISS_SR_PARAM_CITY = "city";
	final public static String ISS_SR_PARAM_WAP_PROXY = "wap_proxy";
	final public static String ISS_SR_PARAM_NET_SUBTYPE = "net_subtype";

	final public static String ISS_SR_PARAM_ACOUS_LANG = "ent";          ///< Recognition languages
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_MANDARIN = "automotiveknife16k"; ///< Mandarin by default
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_ENGLISH = "sms-en16k";         ///< English
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_CANTONESE = "cantonese16k";     ///< Cantonese
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_LMZ = "lmz16k";         ///< Sichuan dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_HENANESE = "henanese16k";        ///< Henan dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_DONGBEIESE = "dongbeiese16k";     ///< Dongbei dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_SHANDONGNESE = "shandongnese16k";  ///< Shandong dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_SHANXINESE = "shanxinese16k";    ///< Shanxi dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_HEFEINESE = "hefeinese16k";      ///< Hefei dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_NANCHANGNESE = "nanchangnese16k";    ///< Nanchang dialect (JIangxi province)
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_CHANGSHANESE = "changshanese16k";   ///< Changsha dialect (Hunan province)
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_WUHANESE = "wuhanese16k";      ///< Wuhan dialect (Hubei province)
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_MINNANESE = "minnanese16k";     ///< Minnan dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_SHANGHAINESE = "shanghainese16k";  ///< Shanghai dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_TIANJINESE = "tianjinese16k";     ///< Tianjin dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_NANJING = "nankinese16k";      ///< Nanjing dialect
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_TAIYUANESE = "taiyuanese16k";      ///< Taiyuan dialect (Shanxi province)
	final public static String ISS_SR_PARAM_ACOUS_LANG_VALUE_UYGHUR = "uyghur16k";          ///< Uyghur

	final public static String ISS_SR_PARAM_TRACE_LEVEL = "tracelevel";
	final public static String ISS_SR_PARAM_TRACE_LEVEL_VALUE_NONE = "none";
	final public static String ISS_SR_PARAM_TRACE_LEVEL_VALUE_ERROR = "error";
	final public static String ISS_SR_PARAM_TRACE_LEVEL_VALUE_INFOR = "infor";
	final public static String ISS_SR_PARAM_TRACE_LEVEL_VALUE_DEBUG = "debug";

	final public static String ISS_SR_PARAM_DOUL_MIC_MAE_DENOISE = "DoulMicMaeDeNoise";
	final public static String ISS_SR_PARAM_SINGLE_MIC_DENOISE = "SingleMicDeNoise";
	final public static String ISS_SR_PARAM_BARGE_IN = "BargeIn";
	final public static String ISS_SR_PARAM_VALUE_ON = "on";                  ///< On
	final public static String ISS_SR_PARAM_VALUE_OFF = "off";                 ///< Off
	final public static String ISS_SR_PARAM_TMP_LOG_DIR = "TmpLogDir";              ///< Set tmp log directory for debugging

	final public static int ISS_SR_PARAM_LOG_LEVEL = (0X00000506);
	final public static String ISS_SR_VOLUME_LOG_LEVEL_ALL = "-1";    // all info
	final public static String ISS_SR_VOLUME_LOG_LEVEL_NONE = "0";    // none
	final public static String ISS_SR_VOLUME_LOG_LEVEL_CRIT = "1";    // critical info
	final public static String ISS_SR_VOLUME_LOG_LEVEL_ERROR = "2";    // error info
	final public static String ISS_SR_VOLUME_LOG_LEVEL_WARNING = "4";    // warnint info

	final public static int ISS_SR_PARAM_LOG_OUTPUT = (0X00000507);
	final public static String ISS_SR_VOLUME_LOG_OUTPUT_NONE = ("0");    // none
	final public static String ISS_SR_VOLUME_LOG_OUTPUT_FILE = ("1");    // file
	final public static String ISS_SR_VOLUME_LOG_OUTPUT_CONSOLE = ("2");    // console（except for android）
	final public static String ISS_SR_VOLUME_LOG_OUTPUT_DEBUGGER = ("4");    // debugger
	final public static String ISS_SR_VOLUME_LOG_OUTPUT_MSGBOX = ("8");    // message box
	
	// Log FileName
	final public static int ISS_SR_PARAM_LOG_FILE_NAME = (0X00000508);

	//message
	final public static int ISS_SR_MSG_InitStatus = 20000;
	final public static int ISS_SR_MSG_UpLoadDictToLocalStatus = 20001;
	final public static int ISS_SR_MSG_UpLoadDictToCloudStatus = 20002;
	final public static int ISS_SR_MSG_VolumeLevel = 20003;
	final public static int ISS_SR_MSG_ResponseTimeout = 20004;
	final public static int ISS_SR_MSG_SpeechStart = 20005;
	final public static int ISS_SR_MSG_SpeechTimeOut = 20006;
	final public static int ISS_SR_MSG_SpeechEnd = 20007;
	final public static int ISS_SR_MSG_Error = 20008;
	final public static int ISS_SR_MSG_Result = 20009;
	final public static int ISS_SR_MSG_LoadBigSrResStatus = 20010;
	final public static int ISS_SR_MSG_ErrorDecodingAudio = 20011;
	final public static int ISS_SR_MSG_PreResult = 20012;
	final public static int ISS_SR_MSG_CloudInitStatus = 20013;
	final public static int ISS_SR_MSG_RealTimeResult = 20014;
	final public static int ISS_SR_MSG_WaitingForCloudResult = 20018;
	final public static int ISS_SR_MSG_Res_Update_Start = 20019;
	final public static int ISS_SR_MSG_Res_Update_End = 20020;
	final public static int ISS_SR_MSG_WaitingForLocalResult = 20021;
	final public static int ISS_SR_MSG_STKS_Result = 20022;
	final public static int ISS_SR_MSG_ONESHOT_MVWResult = 20023;


	public static native int setMachineCode(String code);
	
	public static native int setSerialNumber(String serialNumber);

	public static native int getActiveKey(String resDir);

	public static native int activate(String resDir);

	public static native int create(String resDir, ISRListener iSRListener);
	
	public static native int createEx(int iAcousLang, String resDir, ISRListener iSRListener);

	public static native int start(String szScene, int iMode, String szCmd);

	public static native int uploadDict(String szList, int bOnlyUploadToCloud);

	public static native int setParam(String szParam, String szParamValue);

	public static native int appendAudioData(byte[] audioBuffer, int nNumOfByte);

	public static native int endAudioData();

	public static native int stop();

	public static native int destroy();
	
	public static native int checkGrmBuilding(String dictName, String grmId, int[] outBytes);
	
	public static native int setMvwKeyWords(int nMvwScene, String szKeyWords);
	
	public static native String mspSearch(String szText, String szExternParam);
	public static native String localNli(String szText, String szScene);

}
