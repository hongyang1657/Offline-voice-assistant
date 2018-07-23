package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hongy on 2017/10/23.
 */

public class CreateMessageIdUtils {
    public static String getMessageId(String userId, Context context){

        StringBuilder sb=new StringBuilder(userId);
        sb.append(System.currentTimeMillis()+"");
        PhoneUtils.init(context);
        sb.append(Mac.getMac());
        String messageId= MD5Util(sb.toString());
        return messageId;
    }
    public static String MD5Util(String msg){
        String messageId=null;
        try {
            MessageDigest md5= MessageDigest.getInstance("MD5");
            byte[] result=md5.digest(msg.getBytes());
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < result.length; offset++) {
                int i = result[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            messageId = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return messageId;
    }
}
