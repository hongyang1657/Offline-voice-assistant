/*
 * Created by baoleiwei on 17-5-9 下午3:42
 * Copyright (c) 2017. All rights reserved.
 *
 *
 */

package fitme.ai.zotyeautoassistant.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

import fitme.ai.zotyeautoassistant.utils.L;


/**
 * Created by hongy on 2017/5/9.
 */

public class MusicPlayerService extends Service {

    public static final int PLAT_MUSIC = 1;
    public static final int PAUSE_MUSIC = 2;
    public static final int RESUME_MUSIC = 3;
    public static final int NEXT_MUSIC = 4;
    public static final int STOP_MUSIC = 5;
    public static final int REDUCE_MUSIC_VOLUME = 6;   //把音乐声音调小
    public static final int RECOVER_MUSIC_VOLUME = 7;   //恢复音乐声音
    public static final int PLAY_INFORM = 8;  //播放通知内容（闹钟，提醒，倒计时）

    public static final int PAUSE_MUSIC_BY_GESTURE = 9;    //手势暂停/播放音乐
    public static final int RESUME_MUSIC_BY_GESTURE = 10;


    //用于播放音乐等媒体资源
    private MediaPlayer mediaPlayer;
    //标志判断播放歌曲是否是停止之后重新播放，还是继续播放
    private boolean isCreatPlayer = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i("service-----------------------------onCreate");
        isCreatPlayer = true;

    }

    /**
     * 在此方法中，可以执行相关逻辑，如耗时操作
     * @param intent :由Activity传递给service的信息，存在intent中
     * @param flags ：规定的额外信息
     * @param startId ：开启服务时，如果有规定id，则传入startid
     * @return 返回值规定此startservice是哪种类型，粘性的还是非粘性的
     *          START_STICKY:粘性的，遇到异常停止后重新启动，并且intent=null
     *          START_NOT_STICKY:非粘性，遇到异常停止不会重启
     *          START_REDELIVER_INTENT:粘性的，重新启动，并且将Context传递的信息intent传递
     * 此方法是唯一的可以执行很多次的方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("收到播放指令:"+intent.getIntExtra("type",-1));
        String songUrl = intent.getStringExtra("songUrl");
        int position = intent.getIntExtra("position",0);     //播放音乐的时候跳到position的位置
        final String playerType = intent.getStringExtra("playerType");
        String playerSecond = intent.getStringExtra("playerSecond");
        switch (intent.getIntExtra("type",-1)){
            case PLAT_MUSIC:
                L.i("播放音乐！！！！！！！！！！！！！！！！！");
                boolean isTTSing = intent.getBooleanExtra("isTTSing",false);
                isCreatPlayer = true;

                if (mediaPlayer==null){
                    mediaPlayer = new MediaPlayer();
                    //为播放器添加播放完成时的监听器
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (isCreatPlayer){
                                if (!"通知".equals(playerType)){
                                    L.i("当前歌曲播放完毕，下一首"+mp.isPlaying());
                                    //发送广播
                                    Intent intent = new Intent();
                                    intent.putExtra("mediaPlayerState","next");
                                    intent.setAction("fitme.ai.service.MusicPlayerService");
                                    sendBroadcast(intent);
                                }

                            }

                        }
                    });
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            L.i("onError------------音频无法播放");
                            //TODO 遇到不能播放的情况，请求下一曲

                            return false;
                        }
                    });
                }

                Intent intent1 = new Intent();
                intent1.putExtra("mediaPlayerState","play");
                intent1.putExtra("currentPosition",(double)position/(double) 100);
                intent1.putExtra("currentUrl",songUrl);
                intent1.setAction("fitme.ai.service.MusicPlayerService");
                sendBroadcast(intent1);
                playMusic(songUrl,position,isTTSing);
                break;
            case PAUSE_MUSIC:
                L.i("暂停播放音乐！！！！！！！！！！！！！！！！！");
                //播放器不为空，并且正在播放
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    if ("通知".equals(playerType)){
                        //停止播放
                        mediaPlayer.stop();
                        mediaPlayer = null;
                    }else {
                        mediaPlayer.pause();
                        double positionPercent;
                        positionPercent = (double) mediaPlayer.getCurrentPosition()/(double)mediaPlayer.getDuration();
                        Intent intent2 = new Intent();
                        intent2.putExtra("mediaPlayerState","pause");
                        intent2.putExtra("currentPosition",positionPercent);
                        intent2.putExtra("currentUrl",songUrl);
                        intent2.setAction("fitme.ai.service.MusicPlayerService");
                        sendBroadcast(intent2);
                    }
                }
                break;
            case RESUME_MUSIC:
                if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
                    L.i("继续播放音乐！！！！！！！！！！！！！！！！！");
                    mediaPlayer.setVolume(1f,1f);
                    mediaPlayer.start();
                    double positionPercent;
                    positionPercent = (double) mediaPlayer.getCurrentPosition()/(double)mediaPlayer.getDuration();
                    Intent intent3=new Intent();
                    intent3.putExtra("mediaPlayerState","resume");
                    intent3.putExtra("currentPosition",positionPercent);
                    intent3.putExtra("currentUrl",songUrl);
                    intent3.setAction("fitme.ai.service.MusicPlayerService");
                    sendBroadcast(intent3);
                }
                break;
            case NEXT_MUSIC:
                L.i("下一曲音乐！！！！！！！！！！！！！！！！！");
                playMusic(songUrl,position,false);
                break;
            case STOP_MUSIC:
                if (mediaPlayer!=null){
                    //停止之后要开始播放音乐
                    double positionPercent;
                    positionPercent = (double) mediaPlayer.getCurrentPosition()/(double)mediaPlayer.getDuration();
                    mediaPlayer.stop();
                    mediaPlayer = null;
                    Intent intent4=new Intent();
                    intent4.putExtra("mediaPlayerState","stop");
                    intent4.putExtra("currentPosition",positionPercent);
                    intent4.putExtra("currentUrl",songUrl);
                    intent4.setAction("fitme.ai.service.MusicPlayerService");
                    sendBroadcast(intent4);
                }
                break;
            case REDUCE_MUSIC_VOLUME:
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    //把声音调小
                    mediaPlayer.setVolume(0.1f,0.1f);
                }
                break;
            case RECOVER_MUSIC_VOLUME:
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    //把声音调正常
                    mediaPlayer.setVolume(1f,1f);
                }
                break;
            case PAUSE_MUSIC_BY_GESTURE:
                break;
            case RESUME_MUSIC_BY_GESTURE:
                break;
            case PLAY_INFORM:
                isCreatPlayer = true;
                mediaPlayer = new MediaPlayer();
                //为播放器添加播放完成时的监听器
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (isCreatPlayer){
                            if ("通知".equals(playerType)){
                                L.i("通知播放完毕"+mp.isPlaying());

                            }

                        }

                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        L.i("onError------------音频无法播放");
                        //TODO 遇到不能播放的情况，请求下一曲

                        return false;
                    }
                });

                playInform(songUrl,playerSecond);
                break;

        }
        return START_NOT_STICKY;
    }

    /**
     * 播放内容 url
     * @param songUrl
     * @param position
     * @param isTTSing
     */
    private void playMusic(final String songUrl, final int position, final boolean isTTSing){
        //重置mediaplayer
        mediaPlayer.reset();
        //将需要播放的资源与之绑定
        try {
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mediaPlayer.prepare();//同步的准备方法。
            mediaPlayer.prepareAsync();   //异步准备
            //是否循环播放
            mediaPlayer.setLooping(false);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    L.i("正在准备播放："+songUrl+"音频长度:"+mediaPlayer.getDuration());
                    mediaPlayer.seekTo((int) (mediaPlayer.getDuration()*position/100));
                    mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                        @Override
                        public void onSeekComplete(MediaPlayer mp) {
                            if (isTTSing){
                                mediaPlayer.setVolume(0.1f,0.1f);
                            }else {
                                mediaPlayer.setVolume(1f,1f);
                            }
                            mediaPlayer.start();
                            L.i("准备完毕，开始播放");
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放通知 url
     */
    private void playInform(String informUrl, String playerSecond){
        int informSecond = Integer.valueOf(playerSecond);
        //重置mediaplayer
        mediaPlayer.reset();
        //将需要播放的资源与之绑定
        try {
            mediaPlayer.setDataSource(informUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mediaPlayer.prepare();//同步的准备方法。
            mediaPlayer.prepareAsync();   //异步准备
            //是否循环播放
            mediaPlayer.setLooping(false);
            L.i("通知内容长度："+informSecond+"音频长度:"+mediaPlayer.getDuration());
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                        @Override
                        public void onSeekComplete(MediaPlayer mp) {
                            mediaPlayer.setVolume(1f,1f);
                            mediaPlayer.start();
                            L.i("通知准备完毕，开始播放");
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
