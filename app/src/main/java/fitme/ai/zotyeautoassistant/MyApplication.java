package fitme.ai.zotyeautoassistant;

import android.app.Application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fitme.ai.zotyeautoassistant.view.FloatingView;

/**
 * Created by 69441 on 2017/4/6.
 */
public class MyApplication extends Application {


    //去重消息的列表
    private ArrayList<String> msgId = new ArrayList<>();
    //消息分类处理列表
    private List<String> messageId = new LinkedList<>();
    //场景拦截器
    public List<String> sceneSpeechs = new ArrayList<>();
    //悬浮窗
    private FloatingView mFloatingView;


    private static MyApplication instance;
    public static MyApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        //mFloatingView = new FloatingView(this);
    }

    public FloatingView getmFloatingView() {
        return mFloatingView;
    }

    public void setmFloatingView(FloatingView mFloatingView) {
        this.mFloatingView = mFloatingView;
    }

    public ArrayList<String> getMsgId() {
        return msgId;
    }

    public void setMsgId(ArrayList<String> msgId) {
        this.msgId = msgId;
    }

    public List<String> getMessageId() {
        return messageId;
    }

    public void setMessageId(List<String> messageId) {
        this.messageId = messageId;
    }


    public List<String> getSceneSpeechs() {
        return sceneSpeechs;
    }

    public void setSceneSpeechs(List<String> sceneSpeechs) {
        this.sceneSpeechs = sceneSpeechs;
    }


}
