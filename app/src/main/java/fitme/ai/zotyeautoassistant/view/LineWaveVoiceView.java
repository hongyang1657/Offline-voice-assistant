package fitme.ai.zotyeautoassistant.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fitme.ai.zotyeautoassistant.R;

/**
 * 语音录制的动画效果
 */
public class LineWaveVoiceView extends View {

    //    private MediaRecorder mediaRecorder;
    //线程池
    private ExecutorService executorService;
    private int volume;

    private Paint paint;
    //矩形波纹颜色
    private int lineColor;
    //矩形波纹宽度
    private float lineWidth;
    private float textSize;
    //    private static final String DEFAULT_TEXT = " 倒计时 9:59 ";
    private static final String DEFAULT_TEXT = "";
    private String text = DEFAULT_TEXT;
    private int textColor;
    private boolean isStart = false;
    private Runnable mRunable;

    private int LINE_W = 9;//默认矩形波纹的宽度，9像素, 原则上从layout的attr获得
    private int MIN_WAVE_H = 2;//最小的矩形线高，是线宽的2倍，线宽从lineWidth获得
    private int MAX_WAVE_H = 12;//最高波峰，是线宽的4倍

    //默认矩形波纹的高度，总共10个矩形，左右各有10个
    private int[] DEFAULT_WAVE_HEIGHT = {2, 3, 4, 3, 2, 2, 2, 2, 2, 2};
    private LinkedList<Integer> mWaveList = new LinkedList<>();

    private RectF rectRight = new RectF();//右边波纹矩形的数据，10个矩形复用一个rectF
    private RectF rectLeft = new RectF();//左边波纹矩形的数据

    LinkedList<Integer> list = new LinkedList<>();

    private static final int UPDATE_INTERVAL_TIME = 100;//100ms更新一次
    private float maxAmp;

    public LineWaveVoiceView(Context context) {
        super(context);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineWaveVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        executorService = Executors.newCachedThreadPool();
        paint = new Paint();
        resetList(list, DEFAULT_WAVE_HEIGHT);

        mRunable = new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    refreshElement(volume);//初始化振幅，波纹大小
                    try {
                        Thread.sleep(UPDATE_INTERVAL_TIME);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    postInvalidate();//失效
                }
            }
        };
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LineWaveVoiceView);
        lineColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceLineColor, Color.parseColor("#1EC4BD"));
        lineWidth = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceLineWidth, LINE_W);
        textSize = mTypedArray.getDimension(R.styleable.LineWaveVoiceView_voiceTextSize, 42);
        textColor = mTypedArray.getColor(R.styleable.LineWaveVoiceView_voiceTextColor, Color.parseColor("#666666"));
        mTypedArray.recycle();

//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaRecorder.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int widthcentre = getWidth() / 2;
        int heightcentre = getHeight() / 2;

        //更新时间
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);
        canvas.drawText(text, widthcentre - textWidth / 2, heightcentre - (paint.ascent() + paint.descent()) / 2, paint);

        //更新左右两边的波纹矩形
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(lineWidth);
        paint.setAntiAlias(true);
        try {
            if (list != null) { // TODO: 2017/12/25 闪退报空，暂时在此做非空校验
                for (int i = 0; i < 10; i++) {
                    //右边矩形
                    rectRight.left = widthcentre + 2 * i * lineWidth + textWidth / 2 + lineWidth;
                    rectRight.top = heightcentre - list.get(i) * lineWidth / 2;
                    rectRight.right = widthcentre + 2 * i * lineWidth + 2 * lineWidth + textWidth / 2;
                    rectRight.bottom = heightcentre + list.get(i) * lineWidth / 2;

                    //左边矩形
                    rectLeft.left = widthcentre - (2 * i * lineWidth + textWidth / 2 + 2 * lineWidth);
                    rectLeft.top = heightcentre - list.get(i) * lineWidth / 2;
                    rectLeft.right = widthcentre - (2 * i * lineWidth + textWidth / 2 + lineWidth);
                    rectLeft.bottom = heightcentre + list.get(i) * lineWidth / 2;

                    canvas.drawRoundRect(rectRight, 6, 6, paint);
                    canvas.drawRoundRect(rectLeft, 6, 6, paint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void refreshElement(int volume) {
//        float maxAmp = AudioRecordManager.getInstance().getMaxAmplitude(); // TODO: 2017/12/12 传入声音的最大振幅
//        float maxAmp = getMaxAmplitude(); // TODO: 2017/12/12 传入声音的最大振幅
//        float maxAmp = volume * 1.0f / 327680;
        float maxAmp = ((float) volume) * 1.0f / 100;
//        L.i("refreshElement, maxAmp " + maxAmp);
        int waveH = MIN_WAVE_H + Math.round(maxAmp * (MAX_WAVE_H - 2));//wave 在 2 ~ 12 之间
        list.add(0, waveH);
        list.removeLast();
    }

    public synchronized void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public synchronized void startRecord(int volume) {
        this.volume = volume;
        isStart = true;
        executorService.execute(mRunable);
    }

    public synchronized void stopRecord() {
        isStart = false;
        mWaveList.clear();
        resetList(list, DEFAULT_WAVE_HEIGHT);
        text = DEFAULT_TEXT;
        postInvalidate();
    }

    private void resetList(List list, int[] array) {
        list.clear();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
    }

    public void getVolumeChange(int volume) {
        //获取录音大小
        maxAmp = ((float) volume) * 1.0f / 32768;
    }

//    /**
//     * 获得录音的音量，范围 0-32767, 归一化到0 ~ 1
//     *
//     * @return
//     */
//    public float getMaxAmplitude() {
//        return mediaRecorder.getMaxAmplitude() * 1.0f / 32768;
//    }
}