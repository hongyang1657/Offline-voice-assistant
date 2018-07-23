package fitme.ai.zotyeautoassistant;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

public class FloatingManager {
    private WindowManager mWindowManager;
    private static FloatingManager mInstance;
    private Context mContext;

    public FloatingManager(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    public static FloatingManager getInstance(Context context){
        if (mInstance==null){
            mInstance = new FloatingManager(context);
        }
        return mInstance;
    }

    /**
     * 添加悬浮窗
     * @param view
     * @param params
     * @return
     */
    public boolean addView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.addView(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除悬浮窗
     * @param view
     * @return
     */
    public boolean removeView(View view) {
        try {
            mWindowManager.removeView(view);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新悬浮窗参数
     * @param view
     * @param params
     * @return
     */
    public boolean updateView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.updateViewLayout(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
