package fitme.ai.zotyeautoassistant.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.iflytek.speech.ISSErrors;
import com.iflytek.speech.ITtsListener;
import com.iflytek.speech.NativeHandle;
import com.iflytek.speech.libisstts;

import java.util.LinkedList;
import java.util.List;

import fitme.ai.zotyeautoassistant.MyApplication;
import fitme.ai.zotyeautoassistant.tts.ITTSPlayListener;
import fitme.ai.zotyeautoassistant.tts.TTSPlayer;
import fitme.ai.zotyeautoassistant.utils.FlightControlContants;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.UDPSocketCommand;

import static fitme.ai.zotyeautoassistant.MainActivity.byteMerger;
import static fitme.ai.zotyeautoassistant.utils.Constants.AWAIT_WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.FITME_SERVICE_COMMUNICATION;
import static fitme.ai.zotyeautoassistant.utils.Constants.TAG;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_CONTROL;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_PAUSE;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_PLAY_END;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_RESUME;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_START;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_STATE;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_STOP;
import static fitme.ai.zotyeautoassistant.utils.Constants.TTS_TEXT;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP;
import static fitme.ai.zotyeautoassistant.utils.Constants.WAKE_UP_STATE;
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_COMMAND_0;
import static fitme.ai.zotyeautoassistant.utils.FlightControlContants.FRAME_COMMAND_1;


public class TtsService extends Service{

    //speech列表
    private List<String> speechList;
    private MBroadcastReceiver mBroadcastReceiver;
    private final NativeHandle mNativeHandle = new NativeHandle();
    TTSPlayer mPlayer = null;
    AudioPlayWorking mAudioPlayWorking = new AudioPlayWorking();
    Thread mThread = null;
    private Object mSync = new Object();
    ENGINESTATE mEngineState = ENGINESTATE.TTS_ES_FREE;
    enum ENGINESTATE {
        TTS_ES_RUNNING,
        TTS_ES_FREE,
        TTS_ES_PAUSE
    }

    private synchronized ENGINESTATE getEngineState(){
        return mEngineState;
    }
    private synchronized void setEngineState(ENGINESTATE state){
        mEngineState = state;
    }
    private Intent intentMusic;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initTTS();
        intentMusic = new Intent(this,MusicPlayerService.class);
        L.i("创建TtsService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        libisstts.stop(mNativeHandle);
        libisstts.destroy(mNativeHandle);
        L.i("销毁TtsService");
    }

    private void initTTS(){
        speechList = new LinkedList<>();
        //注册广播
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        registerReceiver(mBroadcastReceiver,intentFilter);

        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iflytek/res/tts/";

        int iErr = libisstts.initRes(strPath, 0);
        libisstts.create(mNativeHandle, mTtsListener);

        //初始化播放器
        mPlayer = new TTSPlayer(new ITTSPlayListener() {
            @Override
            public void playStart() {
                Log.i(TAG, "playStart:开始播报 ");
                //sendBroadcast(TTS_STATE,TTS_PLAY_START);
                playingmusic(MusicPlayerService.REDUCE_MUSIC_VOLUME);  //减小音乐音量
            }

            @Override
            public void playEnd() {
                Log.i(TAG, "playEnd:播报完成 ");
                playingmusic(MusicPlayerService.RECOVER_MUSIC_VOLUME);  //恢复音乐音量
                sendBroadcast(TTS_STATE,TTS_PLAY_END);
                MyApplication.getInstance().getmFloatingView().hide();
                //播放队列中的下一句
                if (speechList.size()>0){
                    libisstts.start(mNativeHandle, speechList.get(0));
                    speechList.remove(0);
                }

            }
        });

        if(mPlayer.init(AudioManager.STREAM_NOTIFICATION) == 0){
            //Log.i(TAG, "initTTS: 播放器初始化完成");
        }else{
            Log.i(TAG, "initTTS: 播放器初始化失败");
        }

        mThread = new Thread(mAudioPlayWorking, "AudioPlayWorkingThread");
        mThread.start();
    }

    private ITtsListener mTtsListener = new ITtsListener() {
        @Override
        public void onDataReady() {
            Log.i(TAG, "ITtsListener -> onDataReady");
            setEngineState(ENGINESTATE.TTS_ES_RUNNING);
            mPlayer.start();
            synchronized(mSync){
                mSync.notifyAll();
            }
        }

        @Override
        public void onProgress(int arg1, int arg2) {
            Log.i(TAG, "ITtsListener -> onProgress : " + arg1 + " , " + arg2);
        }
    };

    //广播接收
    private class MBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String tts_text = intent.getStringExtra(TTS_TEXT);
            int tts_control = intent.getIntExtra(TTS_CONTROL,TTS_START);
            int wakeUp = intent.getIntExtra(WAKE_UP_STATE,AWAIT_WAKE_UP);

            if (wakeUp==WAKE_UP){
                //L.i("唤醒了！！！！清空待播放tts列表");
                speechList.clear();
            }

            switch (tts_control){
                case TTS_START:
                    if (null!=tts_text){
                        Log.i(TAG, "btnStartClicked: libisstts.start:"+tts_text);
                        if (getEngineState() == ENGINESTATE.TTS_ES_RUNNING){
                            //TODO 几种逻辑：1，新TTS打断旧TTS；2，顺序播放
                            /*libisstts.stop(mNativeHandle);
                            setEngineState(ENGINESTATE.TTS_ES_FREE);
                            mPlayer.stop();
                            Log.i(TAG, "onClick: 合成终止");*/

                            //2.顺序播放,后来的speech加入队列
                            speechList.add(tts_text);

                        }else {
                            libisstts.start(mNativeHandle, tts_text);
                            sendRedLight();
                        }
                    }
                    break;
                case TTS_PAUSE:
                    if (getEngineState() == ENGINESTATE.TTS_ES_RUNNING){
                        setEngineState(ENGINESTATE.TTS_ES_PAUSE);
                        mPlayer.pause();
                    }
                    break;
                case TTS_RESUME:
                    if (getEngineState() == ENGINESTATE.TTS_ES_PAUSE){
                        setEngineState(ENGINESTATE.TTS_ES_RUNNING);
                        mPlayer.resume();
                        synchronized(mSync){
                            mSync.notifyAll();
                        }
                    }
                    break;
                case TTS_STOP:
                    Log.i(TAG, "btn clicked : btn_tts_stop");
                    if (getEngineState() == ENGINESTATE.TTS_ES_RUNNING){
                        libisstts.stop(mNativeHandle);
                        setEngineState(ENGINESTATE.TTS_ES_FREE);
                        mPlayer.stop();
                        Log.i(TAG, "ttsClick: 合成终止");
                    }
                    break;
                default:
                    break;
            }

        }
    }

    //发送广播
    private void sendBroadcast(String key,int value){
        Intent intent = new Intent();
        intent.setAction(FITME_SERVICE_COMMUNICATION);
        intent.putExtra(key,value);
        sendBroadcast(intent);
    }

    private void playingmusic(int type) {
        //启动服务，播放音乐
        intentMusic.putExtra("type",type);
        startService(intentMusic);
    }


    private class AudioPlayWorking implements Runnable {
        private final String mTag = "AudioPlayWorking";
        private boolean mExit = false;

        public synchronized void exit() {
            mExit = true;
        }

        private synchronized boolean exitFlag() {
            return mExit;
        }

        @Override
        public void run() {
            Log.i(TAG, "AudioPlayWorking running");
            int minBufferSize = mPlayer.getMinBufferSize();
            byte[] buffer = new byte[minBufferSize];
            int[] bufferSize = new int[1];
            while (!exitFlag()) {
                Log.i(mTag, "AudioPlayWorking waitting ");
                synchronized (mSync) {
                    try {
                        mSync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(mTag, "AudioPlayWorking run");

                    while(getEngineState() == ENGINESTATE.TTS_ES_RUNNING){
                        //Log.i(TAG, "run: libisstts.getAudioData:"+minBufferSize);
                        libisstts.getAudioData(mNativeHandle, buffer, minBufferSize, bufferSize);
                        if (bufferSize[0] > 0){
                            Log.i(mTag, "libisstts.getAudioData size = " + bufferSize[0]);
                            mPlayer.appendAudioData(buffer, bufferSize[0]);
                        }
                        if (mNativeHandle.err_ret == ISSErrors.ISS_ERROR_TTS_COMPLETED){
                            mPlayer.appendAudioDataEnd();
                            setEngineState(ENGINESTATE.TTS_ES_FREE);
                            break;
                        } else if (bufferSize[0] == 0){
                            try {
                                Thread.sleep(20);
                                Log.i(mTag,"tts engine sleep 20");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendGreenLight(){
        UDPSocketCommand.getInstance().sendUdpAndWaitRes(byteMerger(byteMerger(FRAME_COMMAND_0,FRAME_COMMAND_1), FlightControlContants.LISTENNING));
    }

    private void sendRedLight(){
        UDPSocketCommand.getInstance().sendUdpAndWaitRes(byteMerger(byteMerger(FRAME_COMMAND_0,FRAME_COMMAND_1), FlightControlContants.DISPOSING));
    }
}
