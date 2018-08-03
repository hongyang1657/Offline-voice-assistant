package fitme.ai.zotyeautoassistant.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.iflytek.speech.ISSErrors;
import com.iflytek.speech.NativeHandle;
import com.iflytek.speech.libissmvw;
import com.iflytek.speech.mvw.IMVWListener;

import java.io.File;
import java.util.ArrayList;

import fitme.ai.zotyeautoassistant.MyApplication;
import fitme.ai.zotyeautoassistant.utils.IAppendAudio;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.SSRecorder;
import fitme.ai.zotyeautoassistant.utils.SoundPlayUtils;

import static fitme.ai.zotyeautoassistant.utils.Contansts.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Contansts.TAG;
import static fitme.ai.zotyeautoassistant.utils.Contansts.TTS_CONTROL;
import static fitme.ai.zotyeautoassistant.utils.Contansts.TTS_STOP;
import static fitme.ai.zotyeautoassistant.utils.Contansts.WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Contansts.WAKE_UP_STATE;

public class ActivateService extends Service implements IAppendAudio{
    int err = ISSErrors.ISS_SUCCESS;
    private NativeHandle h;
    private Context mContext;
    private Intent intentMusic;

    @Override
    public void onCreate() {
        super.onCreate();
        initMVW();
        mContext = this;
        intentMusic = new Intent(this,MusicPlayerService.class);
        L.i("创建----ActivateService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("ActivateService----------onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        err = libissmvw.destroy(h);
        L.i("销毁ActivateService");
    }

    private void initMVW(){
        IMVWListener lis = new IMVWListener(){
            @Override
            public void onMVWWakeup(int nMvwScene, int nMvwId, int nMvwScore, String lParam) {
                Log.i(TAG, "onMVWWakeup: "+lParam);
                sendBroadcast(WAKE_UP_STATE,WAKE_UP);
                sendBroadcast(TTS_CONTROL,TTS_STOP);   //停止播放正在执行的tts
                SoundPlayUtils.getInstance(mContext).playSound(SoundPlayUtils.WAKE_UP_SOUND);
                //降低音乐播放
                playingmusic(MusicPlayerService.REDUCE_MUSIC_VOLUME);
            }
            public void onMVWMsgProc_(long uMsg,long wParam, String lParam){
                Log.i(TAG, "onMVWMsgProc_: "+lParam);
            }
        };
        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iflytek/res/mvw/FirstRes";
        String keyWord = "{\"Keywords\": [{\"KeyWordId\": 0,\"KeyWord\": \"你好爱芽\"},{\"KeyWordId\": 1,\"KeyWord\": \"你好语音助理\"}]}";
        h = new NativeHandle();
        err = libissmvw.create(h, strPath, lis);
        //Log.i(TAG, "init:create "+err);
        err = libissmvw.setMvwKeyWords(h, 1, keyWord);
        //Log.i(TAG, "init:setMvwKeyWords "+err);
        err = libissmvw.start(h, 1);
        //Log.i(TAG, "init:start "+err);
        String Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/iflytek/res/mvw/";
        //int n =initResDir(Path);
        //Log.i(TAG, "init: "+"找到" + n + " 个资源路径");
        //注册录音
        SSRecorder.instance().registRecordType(SSRecorder.RECORDTYPE.RECORD_MVW, this);

        //开启录音（唤醒准备状态）
        SSRecorder.instance().start(SSRecorder.RECORDTYPE.RECORD_MVW);
    }

    //发送广播
    private void sendBroadcast(String key,int value){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key,value);
        sendBroadcast(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean appendAudioData(byte[] buff) {
        if (h == null)
            return false;
        return libissmvw.appendAudioData(h, buff, buff.length) == ISSErrors.ISS_SUCCESS;
    }


    private ArrayList<String> mLstResDir = null;
    private int initResDir(String path){
        mLstResDir = new ArrayList<String>();
        ArrayList<String> lst = new ArrayList<String>();
        lst.add("FirstRes");
        lst.add("SecondRes");
        lst.add("ThirdRes");
        lst.add("FourthRes");
        lst.add("FifthRes");
        lst.add("SixthRes");
        lst.add("SeventhRes");
        lst.add("EighthRes");
        lst.add("NinthRes");

        for (int n = 0; n != lst.size(); n++){
            String folder = new String();
            folder = path + lst.get(n);
            File f = new File(folder);
            if (f.exists()){
                mLstResDir.add(folder);
            }
        }

        return mLstResDir.size();
    }

    private void playingmusic(int type) {
        //启动服务，播放音乐
        intentMusic.putExtra("type",type);
        startService(intentMusic);
    }
}
