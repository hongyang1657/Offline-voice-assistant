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

import fitme.ai.zotyeautoassistant.bean.Status;
import fitme.ai.zotyeautoassistant.bean.StatusWithUrl;
import fitme.ai.zotyeautoassistant.presenter.MediaContinuePresenter;
import fitme.ai.zotyeautoassistant.presenter.MediaNextPresenter;
import fitme.ai.zotyeautoassistant.presenter.MediaPausePresenter;
import fitme.ai.zotyeautoassistant.presenter.MediaReportPresenter;
import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.view.impl.IMediaPlayerView;


/**
 * Created by hongy on 2017/5/9.
 */

public class MusicPlayerService extends Service implements IMediaPlayerView {

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

    private MediaNextPresenter mediaNextPresenter;
    private MediaPausePresenter mediaPausePresenter;
    private MediaContinuePresenter mediaContinuePresenter;
    private MediaReportPresenter mediaReportPresenter;


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
        mediaNextPresenter = new MediaNextPresenter(this);
        mediaPausePresenter = new MediaPausePresenter(this);
        mediaReportPresenter = new MediaReportPresenter(this);
        mediaContinuePresenter = new MediaContinuePresenter(this);
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
        final String songUrl = intent.getStringExtra("songUrl");
        final int position = intent.getIntExtra("position",0);     //播放音乐的时候跳到position的位置
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
                                    L.i("媒体播放器状态next");
                                    mediaNext();
                                    mediaReport(songUrl,position,"on");
                                }

                            }

                        }
                    });
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                            L.i("onError------------音频无法播放");
                            //TODO 遇到不能播放的情况，请求下一曲
                            mediaNext();
                            mediaReport(songUrl,position,"on");
                            return false;
                        }
                    });
                }

                playMusic(songUrl,position,isTTSing);
                mediaReport(songUrl,position,"on");
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
                    mediaContinue();
                    mediaReport(songUrl, (int) (positionPercent*100),"on");
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
                    mediaReport(songUrl, (int) (positionPercent*100),"off");
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

    //请求下一曲
    private void mediaNext(){
        mediaNextPresenter.mediaNext();
    }

    //上传暂停播放状态
    private void mediaPause(int position){
        mediaPausePresenter.mediaPause(position);
    }

    //继续播放
    private void mediaContinue(){
        mediaContinuePresenter.mediaContinue();
    }

    //上传播放器状态
    private void mediaReport(String url,int position,String musicPlayerStatus){
        mediaReportPresenter.mediaReport(url,position,musicPlayerStatus);
    }

    @Override
    public void getMediaNext(StatusWithUrl statusWithUrl) {
        String url = statusWithUrl.getUrl();
        playMusic(url,0,false);
    }

    @Override
    public void getMediaPause(Status responseBody) {

    }

    @Override
    public void getMediaReport(Status responseBody) {

    }

    @Override
    public void getMediaContinue(Status responseBody) {

    }
}
