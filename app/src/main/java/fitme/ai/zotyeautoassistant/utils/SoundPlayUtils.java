package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import fitme.ai.zotyeautoassistant.R;


/**
 * 播放短音效的工具类
 * Created by hongy on 2017/12/28.
 */

public class SoundPlayUtils {

    private static Context mContext;
    private static SoundPool soundPool;
    private static int soundid;
    //短音效文件
    public static final int WAKE_UP_SOUND = R.raw.wake_up_sound;



    public SoundPlayUtils(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
    }

    private static class SoundPlayHolder{
        private static SoundPlayUtils soundPlayUtils = new SoundPlayUtils();
    }

    public static SoundPlayUtils getInstance(Context context){
        mContext = context.getApplicationContext();
        return SoundPlayHolder.soundPlayUtils;
    }

    public void playSound(int resId){
        soundid = soundPool.load(mContext, resId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundid, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
    }


}
