package fitme.ai.zotyeautoassistant.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllocatePortUtil {

    /**
     * 自动化部署，跟服务端同步生成socket端口号
     * @param projectInstanceName
     * @return
     */
    public static int allocatePort(String projectInstanceName){
        BigInteger num;
        String hashcode = encodeAndToHex(projectInstanceName);
        Log.i("result", "allocatePort: "+hashcode);
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(hashcode);
        Log.i("result", "allocatePort:mmmmm :"+m.replaceAll("").trim());
        num = new BigInteger(m.replaceAll("").trim());
        num.intValue();
        Log.i("result", "allocatePort:num :"+num);
        return 30000+num.divideAndRemainder(new BigInteger("10000"))[1].intValue();
    }

    private static String encodeAndToHex(String a){
        String encodeStr = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(a.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


}
