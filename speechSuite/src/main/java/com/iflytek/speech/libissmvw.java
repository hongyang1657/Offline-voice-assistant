package com.iflytek.speech;

import com.iflytek.speech.mvw.IMVWListener;

public class libissmvw {
	private static final String tag = "libissmvw";
	private static libissmvw instance = null;

	static {
		//System.loadLibrary("ichip-jni");
		System.loadLibrary("imvw-jni");
	}
	
	/// Please refer to the configuration files to check the scene ID information and wakeup word ID information.
	final public static int ISS_MVW_SCENE_GLOBAL = 1;        	///< Wakeup scene: global wakeup
	final public static int ISS_MVW_SCENE_CONFIRM = 2;        	///< Wakeup scene: confirm scene
	final public static int ISS_MVW_SCENE_SELECT = 4;         	///< Wakeup scene: select scene
	final public static int ISS_MVW_SCENE_ANSWER_CALL = 8 ;   	///< Wakeup scene: scene
	final public static int ISS_MVW_SCENE_BUILD_GRM	= 16;	     ///< Wakeup scene: scene
	final public static int ISS_MVW_SCENE_ONESHOT = 32;		 	///< Wakeup scence: oneshot
	
	//MVW_LANG
	final public static int ISS_MVW_LANG_CHN = 0;
	final public static int ISS_MVW_LANG_ENG = 1;
	final public static int ISS_MVW_LANG_BUTT = 2;
	
	// Log level.According to the requirement to combine mask.
	final public static int ISS_MVW_PARAM_LOG_LEVEL =(0X00000506);
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_ALL	="-1";	// all info
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_NONE = "0";	// none
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_CRIT ="1";	// critical info
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_ERROR =	"2"	;// error info
	final public static String ISS_MVW_VOLUME_LOG_LEVEL_WARNING	 = "4";// warnint info

	// Log output.According to the requirement to combine mask.
	final public static int ISS_MVW_PARAM_LOG_OUTPUT =(0X00000507);
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_NONE ="0";	// none
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_FILE =	"1";	// file
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_CONSOLE = "2";	// console（except for android）
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_DEBUGGER = "4";	// debugger
	final public static String ISS_MVW_VOLUME_LOG_OUTPUT_MSGBOX	="8";	// message box

	// Log FileName
	final public static int ISS_MVW_PARAM_LOG_FILE_NAME =(0X00000508);
	
	final public static String ISS_MVW_PARAM_AEC = "mvw_enable_aec";
	final public static String ISS_MVW_PARAM_LSA = "mvw_enable_lsa";
	final public static String ISS_MVW_PARAM_TMP_LOG_DIR = "TmpLogDir" ;               ///< Set tmp log directory for debugging
	final public static String ISS_MVW_PARAM_THRESHOLD_LEVEL = 	"mvw_threshold_level";

	final public static String ISS_MVW_PARAM_VALUE_ON = "on"  ;                    ///< On
	final public static String ISS_MVW_PARAM_VALUE_OFF ="off"    ;                 ///< Off
	
	//message
	final public static int ISS_MVW_MSG_InitStatus              =   40000;
	final public static int ISS_MVW_MSG_VolumeLevel             =   40001;
	final public static int ISS_MVW_MSG_Error                   =   40002;
	final public static int ISS_MVW_MSG_Result                  =   40003;
	final public static int ISS_MVW_MSG_Timeout                 =   40004;
	
	public static native int create(NativeHandle nativeHandle, String resDir, IMVWListener iMvwListener);
	public static native int createEx(NativeHandle nativeHandle, String resDir, IMVWListener iMvwListener);

	public static native int destroy(NativeHandle nativeHandle);

	public static native int setThreshold(NativeHandle nativeHandle,int nMvwScene, int nMvwId,
			int threshold);
			
	public static native int setParam(NativeHandle nativeHandle, String szParam, String szParamValue);
	
	public static native int start(NativeHandle nativeHandle,int nMvwScene);
	public static native int addstartscene(NativeHandle nativeHandle,int nMvwScene);

	public static native int appendAudioData(NativeHandle nativeHandle,byte[] AudioBuffer,
			int nNumberOfByte);

	public static native int stop(NativeHandle nativeHandle);
	public static native int stopscene(NativeHandle nativeHandle,int nMvwScene);
	public static native int setMvwKeyWords(NativeHandle nativeHandle,int nMvwScene, String szWords);
	public static native int setMvwDefaultKeyWords(NativeHandle nativeHandle,int nMvwScene);
	public static native int setMvwLanguage(int nLangType);
	public static native boolean isCouldAppendAudioData();
}
