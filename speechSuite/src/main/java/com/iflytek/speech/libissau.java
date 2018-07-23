package com.iflytek.speech;

public class libissau {
	private static final String tag = "libissau";
	
		static {
		System.loadLibrary("iau-jni");
	}
	final public static int ISS_AU_AEC_SINGLE = 1;			///< mono reference signal
	final public static int ISS_AU_AEC_DOUBLE = 2;			///< stereo reference signal
	final public static int ISS_AU_AEC_NONALIGN = 0;		///< non-align BargeIn
	final public static int ISS_AU_AEC_ALIGN = 1;			///< align BargeIn
	final public static int ISS_AU_INVALID = -1;
	final public static int ISS_AU_AEC = 0;
	final public static int ISS_AU_COUNT=1;
	
	final public static String ISSAU_PARAM_AEC_LEAK_SET ="2";
	final public static String ISSAU_AEC_LEAK_VALUE	="10";	//"1"~"100"，default is "10"
	/* AEC out write mode parameters and parameter value */
	final public static String ISSAU_PARAM_AEC_OUTWRITE_MODE ="0";		///< default mode is ISSAU_AEC_OUTWRITE_MODE_MONO
	final public static String ISSAU_AEC_OUTWRITE_MODE_MONO	="0";		///< just get 128 samples of AEC output.( AECOut0 AECOut1   AECout127 )
	final public static String ISSAU_AEC_OUTWRITE_MODE_STEREO_ECHO ="1"	;	///< get 128 samples of AEC output and 128 samples.( AECOut0 Mic0 AECOut1 Mic1   AECout127 Mic127 )
	final public static String ISSAU_AEC_OUTWRITE_MODE_STEREO_REF ="2";		///< get 128 samples of AEC output and 128 samples.( AECOut0 Ref0 AECOut1 Ref1   AECout127 Ref127 )

	/* AEC work mode parameters and parameter value*/
	final public static String ISSAU_PARAM_AEC_WORK_MODEL ="1";		///< defaule mode is ISSAU_AEC_WORK_MODEL_MUSIC
	final public static String ISSAU_AEC_WORK_MODEL_TELEPHONE ="0";		///< telephone model
	final public static String ISSAU_AEC_WORK_MODEL_MUSIC ="1";		///< music model 
		
	public static native int aecCreate(int iRefCount, int iType);
	public static native int setParam(int iModule, String strParam, String strValue);
	public static native int aecProcessAudio(byte[] audioBuf, byte[] refBuf, int iSize, byte[] outBuf, int[] iOutBufSize);
	public static native int aecReset(int iModule);
	public static native int aecDestory();
}