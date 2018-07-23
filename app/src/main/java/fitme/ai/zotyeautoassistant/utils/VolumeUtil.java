package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;
import android.media.AudioManager;


/**
 * Created by hongy on 2018/1/5.
 */

public class VolumeUtil {
    private static VolumeUtil volumeUtilInstance;
    private static AudioManager mAudioManager;
    private static int currentVolume;

    public static VolumeUtil getInstance(Context context){
        if (volumeUtilInstance==null){
            volumeUtilInstance = new VolumeUtil();
            //音量控制,初始化定义
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return volumeUtilInstance;
    }

    /**
     * 设置音量加某一个值
     * @param value 音量调整值，可为负数
     */
    public void setVolumePlus(int value){
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        L.i("当前音量"+currentVolume);
        currentVolume = currentVolume+value;
        if (currentVolume>10){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10, AudioManager.FLAG_SHOW_UI);
        }else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume, AudioManager.FLAG_SHOW_UI);
        }
    }


    /**
     * 设置音量的值
     * @param value 音量
     */
    public void setVolumeValue(int value){
        int mVolume = value;
        if (mVolume==0){
            mVolume = 0;
        } else if(mVolume<6&&mVolume>0){
            mVolume=mVolume+4;
        }else{
            mVolume=10;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mVolume, AudioManager.FLAG_SHOW_UI);
    }


    /**
     * 音量加倍
     */
    public void setVolumeDouble(){
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        L.i("当前音量"+currentVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume*2, AudioManager.FLAG_SHOW_UI);
    }

    /**
     * 音量减半
     */
    public void setVolumeHalf(){
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        L.i("当前音量"+currentVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume/2, AudioManager.FLAG_SHOW_UI);
    }
}
