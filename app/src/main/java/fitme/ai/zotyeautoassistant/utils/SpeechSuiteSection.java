package fitme.ai.zotyeautoassistant.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/8/30.
 */
public class SpeechSuiteSection {

    //界面fragment
    protected Activity mActivity = null;

    //resource path
    protected String mResPath = null;

    public Message createMsg(int arg1, int arg2, String strObj){
        Message msg = new Message();
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = strObj + "\n";
        return msg;
    }

    public Message createMsg(int arg1, int arg2){
        Message msg = new Message();
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = "\n";
        return msg;
    }

    public boolean attachWidget(Activity act){ return true; }
    public void detachWidget(){}


    public String formatJson(String jsonStr){
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    private void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    public String getTime(){
        SimpleDateFormat formatter   =   new SimpleDateFormat("HH:mm:ss.SSS");
        Date curDate =  new Date(System.currentTimeMillis());
        String str = "[ " + formatter.format(curDate) + " ] ➣ ";
        return str;
    }

}
