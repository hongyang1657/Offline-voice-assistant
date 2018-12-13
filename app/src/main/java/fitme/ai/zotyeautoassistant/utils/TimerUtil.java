package fitme.ai.zotyeautoassistant.utils;

import android.os.Handler;

import fitme.ai.zotyeautoassistant.view.impl.TimerEndListener;

public class TimerUtil{

    public static class TimerUtilHolder{
        private static TimerUtil timerUtil = new TimerUtil();
    }
    public static TimerUtil getInstance(){
        return TimerUtilHolder.timerUtil;
    }
    public TimerUtil(){
    }

    private boolean isTimering = false;
    private TimerEndListener listener;
    private int nowTime = 0;
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (nowTime<60){
                nowTime++;
                L.i("计时60秒:"+nowTime);
                handler.postDelayed(task,1000);
                isTimering = true;
            }else {
                //倒计时结束
                nowTime = 0;
                listener.timeEnd();
                isTimering = false;
            }
        }
    };

    public void start(){
        L.i("开始倒计时");
        nowTime = 0;
        handler.removeCallbacks(task);
        handler.post(task);
    }

    public void stop(){
        L.i("停止倒计时");
        nowTime = 0;
        handler.removeCallbacks(task);
        isTimering = false;
    }

    public boolean getTimerState(){
        return isTimering;
    }

    public void setOnTimerEndListener(TimerEndListener listener){
        this.listener = listener;
    }

}
