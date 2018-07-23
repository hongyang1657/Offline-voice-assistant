package fitme.ai.zotyeautoassistant.utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 签名加密工具类
 * Created by blw on 2016/8/26.
 */
public class SignAndEncrypt {

    private static String webUrl2 = "http://www.baidu.com";//百度
    private static String webUrl3 = "http://www.taobao.com";//淘宝
    private static String webUrl4 = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
    private static String webUrl5 = "http://www.360.cn";//360
    private static String webUrl6 = "http://www.beijing-time.org";//beijing-time


    //签名请求，返回生成的十六进制签名字符串
    public static String signRequest(Map<String, Object> params, String api_secret) {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        query.append(api_secret);
        for (String key : keys) {
            String value = String.valueOf(params.get(key));
            if (value != null && value.length() > 0) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5加密
        byte[] bytes;
        query.append(api_secret);
        bytes = encryptMD5(query.toString());

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }


    //敏感信息对称加密
    public static String DesEncrypt(String password, String api_secret) {
        String des_password = null;
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(Arrays.copyOfRange(api_secret.getBytes("UTF-8"), 0, 8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // 实例化加密类，参数为加密方式
            Cipher cipher = Cipher.getInstance("DES");
            // 初始化，此方法可以采用三种方式，按服务器要求来添加,第三个参数为SecureRandom
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            des_password = byte2hex(cipher.doFinal(password.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return des_password;

    }


    //MD5非对称加密
    public static String asymmetricEncryptMd5(String data, String api_secret, String timestamp) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(api_secret);
        stringBuilder.append(data);
        stringBuilder.append(timestamp);
        stringBuilder.append(api_secret);
        return byte2hex(encryptMD5(stringBuilder.toString()));
    }


    //md5加密
    public static byte[] encryptMD5(String data) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            md5.update(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5.digest();
    }


    //字节数组转换为十六进制
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    //检测系统时间是否正确
    public static boolean isTimeStampRight = false;
    public static boolean timeStampTest(){

        try {
            if (String.valueOf(System.currentTimeMillis()).substring(0,10).equals(new GetNetworkTimeTask().execute().get().substring(0,10))){
                isTimeStampRight = true;
            }else {
                isTimeStampRight = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTimeStampRight;
    }

    //获取时间戳,本地时间不对则获取网络时间
    public static String getTimeStamp() {
        //判断网络时间是否等于本地时间
        String str = "";
        if (isTimeStampRight){
            //L.i("使用本地时间");
            long time = System.currentTimeMillis();//获取系统时间当前的时间戳
            str = String.valueOf(time);

        }else{
            //L.i("使用网络时间");
            try {
                str = new GetNetworkTimeTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return str;
    }


    /**
     * 获取网络时间
     */
    public static class GetNetworkTimeTask extends AsyncTask<String, Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            return getWebsiteDatetime(webUrl2);
        }

        @Override
        protected void onPostExecute(String time) {
            super.onPostExecute(time);
        }
    }


    /**
     * 获取指定网站的日期时间 获取时间戳
     *
     * @param webUrl
     */
    private static String getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            return String.valueOf(date.getTime());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
