package fitme.ai.zotyeautoassistant.tts;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by PeiXie on 2016/9/7.
 */


public class TTSPlayer {

    private String mTag = "TTSPlayer";
    private static final int mSampleRateInHz = 16000;
    private static final int mChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
    private static final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static Lock mAudioTrackLock = null;
    private AudioTrack mAudioTrack = null;
    private int mAudioTrackMinBuffSize = 0;
    private int mAudioAppendSize = 0;
    //private int mStreamType = AudioManager.STREAM_NOTIFICATION;
    private int mStreamType = AudioManager.STREAM_MUSIC;     //hy,换成媒体音乐流

    private ITTSPlayListener mTTSPlayListener = null;


    public TTSPlayer(ITTSPlayListener listener) {
        mTTSPlayListener = listener;
    }

    public int init(int streamType) {
        mStreamType = streamType;

        mAudioTrackLock = new ReentrantLock();
        mAudioTrackMinBuffSize = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);

        mAudioTrack = new AudioTrack(mStreamType,
                mSampleRateInHz,
                mChannelConfig,
                mAudioFormat,
                mAudioTrackMinBuffSize * 3,
                AudioTrack.MODE_STREAM);
        if (mAudioTrack == null || mAudioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
            return -1;
        }
        mAudioTrack.setNotificationMarkerPosition(0);

        mAudioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onMarkerReached(AudioTrack track) {
                mAudioTrackLock.lock();
                mAudioTrack.stop();
                mAudioTrack.flush();
                mAudioTrackLock.unlock();
                mAudioTrack.setNotificationMarkerPosition(0);

                mTTSPlayListener.playEnd();
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {

            }
        });

        return 0;
    }

    public int getMinBufferSize(){return mAudioTrackMinBuffSize;}

    public int appendAudioDataEnd(){
        int pos = mAudioAppendSize/mAudioFormat;
        mAudioTrackLock.lock();
        mAudioTrack.setNotificationMarkerPosition(pos);
        mAudioTrackLock.unlock();
        return pos;
    }


    public boolean start() {
        mAudioAppendSize = 0;
        mAudioTrackLock.lock();
        mAudioTrack.flush();
        mAudioTrack.play();
        mAudioTrackLock.unlock();
        mTTSPlayListener.playStart();
        return true;
    }

    public synchronized void appendAudioData(byte[] buff, int size) {
        mAudioAppendSize += size;

        mAudioTrackLock.lock();
        mAudioTrack.write(buff, 0, size);
        mAudioTrackLock.unlock();
        Log.i(mTag, "audio append size = " + mAudioAppendSize);
    }

    public void pause() {
        mAudioTrackLock.lock();
        mAudioTrack.pause();
        mAudioTrackLock.unlock();
    }

    public void resume() {
        mAudioTrackLock.lock();
        mAudioTrack.play();
        mAudioTrackLock.unlock();
    }

    public void stop() {
        mAudioAppendSize = 0;

        mAudioTrackLock.lock();
        mAudioTrack.stop();
        mAudioTrack.flush();
        mAudioTrackLock.unlock();
    }

}
