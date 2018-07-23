package com.iflytek.speech;

/**
 * Created by PeiXie on 2016/9/27.
 */

public class TTSParameter {
  ///< The code page of the input TTS text
	final public static int ISS_TTS_CODEPAGE_GBK = 936;
    final public static int ISS_TTS_CODEPAGE_UTF16LE = 1200;
    final public static int ISS_TTS_CODEPAGE_UTF8 = 65001;
	
	
	final public static int ISS_TTS_RES_MODE_FILE_HANDLE= 0	;/*!< TTS Res load type. by file handle */
	final public static int ISS_TTS_RES_MODE_MEMORY	=	 1;	/*!< TTS Res load type. load to memory */

	final public static int ISS_TTS_PARAM_SPEAKER =(0x00000500);
	
	//tagespeakers
	final public static int ivTTS_ROLE_TIANCHANG = 1;   /**< Tianchang (female, Chinese) */
    final public static int ivTTS_ROLE_WENJING = 2;   /**< Wenjing (female, Chinese) */
    final public static int ivTTS_ROLE_XIAOYAN = 3;     /**< Xiaoyan (female, Chinese) */
    final public static int ivTTS_ROLE_YANPING = 3;     /**< Xiaoyan (female, Chinese) */
    final public static int ivTTS_ROLE_XIAOFENG = 4;    /**< Xiaofeng (male, Chinese) */
    final public static int ivTTS_ROLE_YUFENG = 4;      /**< Xiaofeng (male, Chinese) */
    final public static int ivTTS_ROLE_SHERRI = 5;      /**< Sherri (female, US English) */
    final public static int ivTTS_ROLE_XIAOJIN = 6;     /**< Xiaojin (female, Chinese) */
    final public static int ivTTS_ROLE_NANNAN = 7;     /**< Nannan (child, Chinese) */
    final public static int ivTTS_ROLE_XIAOJING = 8;    /**< Xiaojing (female, Chinese) */
    final public static int ivTTS_ROLE_JIAJIA = 9;      /**< Jiajia (girl, Chinese) */
    final public static int ivTTS_ROLE_YUER = 10;       /**< Yuer (female, Chinese) */
    final public static int ivTTS_ROLE_XIAOQIAN = 11;   /**< Xiaoqian (female, Chinese Northeast) */
    final public static int ivTTS_ROLE_LAOMA = 12;      /**< Laoma (male, Chinese) */
    final public static int ivTTS_ROLE_BUSH = 13;       /**< Bush (male, US English) */
    final public static int ivTTS_ROLE_XIAORONG = 14;   /**< Xiaorong (female, Chinese Szechwan) */
    final public static int ivTTS_ROLE_XIAOMEI = 15;    /**< Xiaomei (female, Cantonese) */
    final public static int ivTTS_ROLE_ANNI = 16;       /**< Anni (female, Chinese) */
    final public static int ivTTS_ROLE_JOHN = 17;       /**< John (male, US English) */
    final public static int ivTTS_ROLE_ANITA = 18;      /**< Anita (female, British English) */
    final public static int ivTTS_ROLE_TERRY = 19;      /**< Terry (female, US English) */
    final public static int ivTTS_ROLE_CATHERINE = 20;  /**< Catherine (female, US English) */
    final public static int ivTTS_ROLE_TERRYW = 21;     /**< Terry (female, US English Word) */
    final public static int ivTTS_ROLE_XIAOLIN = 22;    /**< Xiaolin (female, Chinese) */
    final public static int ivTTS_ROLE_XIAOMENG = 23;   /**< Xiaomeng (female, Chinese) */
    final public static int ivTTS_ROLE_XIAOQIANG = 24;  /**< Xiaoqiang (male, Chinese) */
    final public static int ivTTS_ROLE_XIAOKUN = 25;    /**< XiaoKun (male, Chinese) */
    final public static int ivTTS_ROLE_JIUXU = 51;      /**< Jiu Xu (male, Chinese) */
    final public static int ivTTS_ROLE_DUOXU = 52;      /**< Duo Xu (male, Chinese) */
    final public static int ivTTS_ROLE_XIAOPING = 53;   /**< Xiaoping (female, Chinese) */
    final public static int ivTTS_ROLE_DONALDDUCK = 54; /**< Donald Duck (male, Chinese) */
    final public static int ivTTS_ROLE_BABYXU = 55;     /**< Baby Xu (child, Chinese) */
    final public static int ivTTS_ROLE_DALONG = 56;     /**< Dalong (male, Cantonese) */
    final public static int ivTTS_ROLE_TOM = 57;     /**< Tom (male, US English) */
    final public static int ivTTS_ROLE_MINGMA = 58;		/**< MingMa (male, Chinese) */
	final public static int ivTTS_ROLE_BAOMA = 59;		/**< BaoMa (child, Chinese) */
	final public static int ivTTS_ROLE_BELLA = 60;		/**< Bella (child, US English) */
	final public static int ivTTS_ROLE_USER = 99;		/**< user defined */
	/* The customized pronunciation person VID, ranging from 50,000 to 100,000 */
	final public static int ivTTS_ROLE_XIAORUI = 50001; 	/**< Xiaorui   (female, Chinese) */
	final public static int ivTTS_ROLE_XIAOSHI = 51180;      /**< Xiaoshi   (female, Chinese) */
	final public static int ivTTS_ROLE_XIAOYAO = 50003;      /**< Xiaoyao   (female, Chinese) */
	final public static int ivTTS_ROLE_XIAOYANG = 50004;     /**< Xiaoyang  (male, Chinese) */
	final public static int ivTTS_ROLE_XIAOHONG = 50005;     /**< Xiaohong  (male, Chinese) */
	final public static int ivTTS_ROLE_XIAOHUI = 50006;     /**< Xiaohui   (male, Chinese) */
	final public static int ivTTS_ROLE_XIAOBO = 50007;       /**< Xiaobo    (male, Chinese) */
	final public static int ivTTS_ROLE_XIAOWANZI = 50008;    /**< Xiaowanzi (female, Chinese) */
	final public static int ivTTS_ROLE_XIAOXIN = 50009;      /**< Xiaoxin   (male, Chinese) */
	final public static int ivTTS_ROLE_XIAOMA = 50010;      /**< Xiaoma    (male, Chinese) */
	final public static int ivTTS_ROLE_XIAOLING = 50011;     /**< Xiaoling  (female, Chinese) */
	final public static int ivTTS_ROLE_XIAOXUE = 50110;      /**< Xiaoxue   (female, Chinese) */
	final public static int ivTTS_ROLE_XIAOJIE = 50130;      /**< Xiaojie   (female, Chinese) */
	final public static int ivTTS_ROLE_XIAONUO = 50160;      /**< Xiaonuo   (female, Chinese) */
	final public static int ivTTS_ROLE_YIFENG = 51090;      /**< Yifeng   (female, Chinese) */
	
	final public static int ISS_TTS_PARAM_VOICE_SPEED =(0x00000502); /*!< Used to set the TTS voice speed */
	final public static int ISS_TTS_SPEED_MIN =(-32768)   ;          /*!< The slowest speed */
	final public static int ISS_TTS_SPEED_NORMAL_DEFAULT= (0);       /*!< The normal speed by default */
	final public static int ISS_TTS_SPEED_MAX= (+32767);             /*!< The fastest speed */

	final public static int ISS_TTS_PARAM_VOICE_PITCH =(0x00000503) ;/*!< Used to set the 
	TTS voice pitch */
	final public static int ISS_TTS_PITCH_MIN= (-32768);             /*!< The lowest pitch */
	final public static int ISS_TTS_PITCH_NORMAL_DEFAULT= (0)  ;     /*!< The normal pitch by default */
	final public static int ISS_TTS_PITCH_MAX =(+32767) ;            /*!< The highest pitch */

	final public static int ISS_TTS_PARAM_VOLUME =(0x00000504) ;  /*!< Used to set the TTS voice volume */
	final public static int ISS_TTS_VOLUME_MIN =(-32768) ;        /*!< The minimum volume */
	final public static int ISS_TTS_VOLUME_NORMAL =(0) ;          /*!< The normal volume */
	final public static int ISS_TTS_VOLUME_MAX_DEFAULT= (+32767) ;/*!< The maximum volume by default */

	final public static int ISS_TTS_PARAM_USERMODE=	(0x00000701);		/*!< user's mode */
	/* constants for values of parameter ivTTS_PARAM_USERMODE(ivTTS_PARAM_NAVIGATION_MODE) */
	final public static int ISS_TTS_VOLUME_USE_NORMAL=0	;		/*!< synthesize in the Mode of Normal */
	final public static int ISS_TTS_VOLUME_USE_NAVIGATION=1	;		/*!< synthesize in the Mode of Navigation */
	final public static int ISS_TTS_VOLUME_USE_MOBILE=2		;	/*!< synthesize in the Mode of Mobile */
	final public static int ISS_TTS_VOLUME_USE_EDUCATION=3	;		/*!< synthesize in the Mode of Education */
	final public static int ISS_TTS_VOLUME_USE_TV=4	;		/*!< synthesize in the Mode of TV */

	final public static int ISS_TTS_PARAM_VOLUME_INCREASE=	(0X00000505);		/* volume value increase */
	final public static int ISS_TTS_VOLUME_INCREASE_MIN=0;		/* minimized volume (default) */
	final public static int ISS_TTS_VOLUME_INCREASE_MAX	=10	;	/* maximized volume */

	final public static int ISS_TTS_PARAM_TMP_LOG_DIR  = (0X00000606) ;              /*Set tmp log directory for debugging*/
	
	// Log level.According to the requirement to combine mask.
	final public static int ISS_TTS_PARAM_LOG_LEVEL	= 				0X00000506;
	final public static String ISS_TTS_VOLUME_LOG_LEVEL_ALL	=		"-1";	// all info
	final public static String ISS_TTS_VOLUME_LOG_LEVEL_NONE =		"0";	// none
	final public static String ISS_TTS_VOLUME_LOG_LEVEL_CRIT =		"1";	// critical info
	final public static String ISS_TTS_VOLUME_LOG_LEVEL_ERROR =		"2";	// error info
	final public static String ISS_TTS_VOLUME_LOG_LEVEL_WARNING	=	"4";	// warnint info

	// Log output.According to the requirement to combine mask.
	final public static int ISS_TTS_PARAM_LOG_OUTPUT = 				0X00000507;
	final public static String ISS_TTS_VOLUME_LOG_OUTPUT_NONE =			"0";	// none
	final public static String ISS_TTS_VOLUME_LOG_OUTPUT_FILE =			"1";	// file
	final public static String ISS_TTS_VOLUME_LOG_OUTPUT_CONSOLE =		"2";	// console（except for android）
	final public static String ISS_TTS_VOLUME_LOG_OUTPUT_DEBUGGER =		"4";	// debugger
	final public static String ISS_TTS_VOLUME_LOG_OUTPUT_MSGBOX =		"8";	// message box

	// Log FileName
	final public static int ISS_TTS_PARAM_LOG_FILE_NAME	=(0X00000508);
}