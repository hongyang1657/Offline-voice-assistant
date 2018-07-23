package fitme.ai.zotyeautoassistant.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import fitme.ai.zotyeautoassistant.FloatingManager;
import fitme.ai.zotyeautoassistant.R;
import fitme.ai.zotyeautoassistant.utils.L;

import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_RESPONSE;
import static fitme.ai.zotyeautoassistant.utils.Contansts.ASR_VOLUME;
import static fitme.ai.zotyeautoassistant.utils.Contansts.FITME_SERVICE_COMMUNICATION;

public class FloatingView extends FrameLayout{
    private Context mContext;
    private View mView;
    private TextView tvAsr;
    private LinearLayout floatingView;
    private LineWaveVoiceView lineWaveVoiceView;
    private WindowManager.LayoutParams mParams;
    private FloatingManager mWindowManager;
    private MBroadcastReceiver mBroadcastReceiver;

    public FloatingView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.floating_view,null);
        tvAsr = mView.findViewById(R.id.tv_asr);
        lineWaveVoiceView = mView.findViewById(R.id.line_wave_view);
        floatingView = mView.findViewById(R.id.floating_view);
        mWindowManager = FloatingManager.getInstance(mContext);
        //注册广播
        mBroadcastReceiver = new MBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FITME_SERVICE_COMMUNICATION);
        context.registerReceiver(mBroadcastReceiver,intentFilter);

        initFloatView();
        floatingView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    //广播接收
    private class MBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String asrResponse = intent.getStringExtra(ASR_RESPONSE);
            int volume = intent.getIntExtra(ASR_VOLUME,0);
            if (null!=asrResponse&&!asrResponse.equals("")){
                tvAsr.setText(asrResponse);
            }
            //L.i("音量大小："+volume/20);
            lineWaveVoiceView.startRecord(volume/20);
        }
    }

    private void initFloatView(){
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.BOTTOM;
        mParams.x = 0;
        mParams.y = 0;
        //总是出现在最上层
        //mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //android 8.0 必须使用TYPE_APPLICATION_OVERLAY
        if (Build.VERSION.SDK_INT>=26){
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ;
        }else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        mParams.width = LayoutParams.MATCH_PARENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
    }

    public void show(){
        tvAsr.setText("");
        mWindowManager.addView(mView,mParams);
    }

    public void hide(){
        mWindowManager.removeView(mView);
        lineWaveVoiceView.stopRecord();
    }


}
