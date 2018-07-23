package fitme.ai.zotyeautoassistant.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static fitme.ai.zotyeautoassistant.utils.Contansts.TAG;


/**
 * Created by PeiXie on 2016/12/27.
 */
public class SSRecorder {
    static String mTag = "SSRecorder";

    private static final int DEFAULT_SAMPLE_RATE = 16 * 1000;
    private static final short DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static final int DEFAULT_TIMER_INTERVAL = 40;

    private int mFlag = 0;
    private Lock mFlagLock = new ReentrantLock();
    private AudioRecord mRecorder = null;

    private Object mSignal = new Object();

    private int mBuffSize = 0;
    private Map<RECORDTYPE, IAppendAudio> mMapAppend = new HashMap<RECORDTYPE, IAppendAudio>();

    public enum RECORDTYPE{
        RECORD_SR(1), RECORD_MVW(2);
        private int mType;
        private RECORDTYPE(int type){
            this.mType = type;
        }
    }

    static SSRecorder mThis = null;
    static public SSRecorder instance(){
        if (null == mThis){
            mThis = new SSRecorder();
            mThis.initRecorder(AudioFormat.CHANNEL_IN_MONO);
            mThis.mRecordLoop.start();
        }
        return mThis;
    }

    private SSRecorder(){

    }

    Thread mRecordLoop = new Thread(new Runnable() {
        @Override
        public void run() {
            byte[] buff = new byte[mBuffSize];

            while (true) {
                try {
                    synchronized(mSignal){
                        mSignal.wait();
                    }
                } catch (InterruptedException e) {
                    Log.i(mTag, "signal wait exception.");
                }
                mRecorder.startRecording();
                Log.i(mTag, "RecordLoop map size " + mMapAppend.size());

                mFlagLock.lock();
                int flag = mFlag;
                mFlagLock.unlock();

                while (flag != 0) {
                    int count = mRecorder.read(buff, 0, mBuffSize);
                    if ((mFlag & RECORDTYPE.RECORD_SR.mType) != 0
                            && mMapAppend.containsKey(RECORDTYPE.RECORD_SR)) {
                        mMapAppend.get(RECORDTYPE.RECORD_SR).appendAudioData(buff);
                    }
                    if ((mFlag & RECORDTYPE.RECORD_MVW.mType) != 0
                            && mMapAppend.containsKey(RECORDTYPE.RECORD_MVW)) {
                        //Log.i(TAG, "run: RECORD_MVW");
                        mMapAppend.get(RECORDTYPE.RECORD_MVW).appendAudioData(buff);
                    }
                    mFlagLock.lock();
                    flag = mFlag;
                    mFlagLock.unlock();
                }
            }
        }
    });

    public int registRecordType(RECORDTYPE type, IAppendAudio appendAudio){
        mMapAppend.put(type, appendAudio);
        return mFlag;
    }

    public int start(RECORDTYPE type){
        mFlagLock.lock();
        mFlag |= type.mType;
        mFlagLock.unlock();
        Log.i(mTag, "start mFlag = " +mFlag);
        synchronized(mSignal){
            mSignal.notify();
        }
        return mFlag;
    }
    public int stop(RECORDTYPE type){
        mFlagLock.lock();
        mFlag &= (~type.mType);
        mFlagLock.unlock();
        Log.i(mTag, "stop mFlag = " +mFlag);
        return mFlag;
    }

    private boolean initRecorder(int channel){
        int buffSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLE_RATE, channel, DEFAULT_AUDIO_FORMAT);
        mBuffSize = buffSize;
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, DEFAULT_SAMPLE_RATE,
                                    channel, DEFAULT_AUDIO_FORMAT, mBuffSize * 3);
        return mRecorder != null;
    }

}
