package fitme.ai.zotyeautoassistant.tts;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PeiXie on 2016/9/20.
 */
public class TTSSampleParameter {
    String mTag = "TTSParameter";

    class TTSSampleParameterVolume{
        //默认发音人
        public int mSpeakerId = 9;
        public String mSpeakerName = "jiajia.irf";
        public int mMode = 1;
        public String mModeName = "导航模式";
        public int mSpeed = 0;
        public int mPitch = 0;
        public int mVolume = 0;
        public int mIncrease = 0;
    }

    private Map<String, Integer> mSpeakers = null;
    private Map<String, Integer> mModes = null;

    public TTSSampleParameterVolume mParaCurrent = null;
    public TTSSampleParameterVolume mParaSetting = null;

    public TTSSampleParameter(){
        mSpeakers = new HashMap<String, Integer>();
        mModes = new HashMap<String, Integer>();
        mModes.put("普通模式", 0);
        mModes.put("导航模式", 1);
        mModes.put("手机模式", 2);
        mModes.put("教育模式", 3);
        mModes.put("电视模式", 4);

        mParaCurrent = new TTSSampleParameterVolume();
        mParaSetting = new TTSSampleParameterVolume();
    }

    public boolean loadPara(String cfgPath){
        try {
            FileInputStream inCfg = new FileInputStream(cfgPath);
            int length = inCfg.available();
            byte[] buffer = new byte[length + 1];
            inCfg.read(buffer);

            String strCfg = new String(buffer, "UTF-8");
            JSONObject jsonObj = new JSONObject(strCfg);
            int userMode = jsonObj.getInt("usermode");
            int cacheSize = jsonObj.getInt("CacheSize");
            JSONArray jsonArr = jsonObj.getJSONArray("resource");

            for (int n = 0; n != jsonArr.length(); n++){
                JSONObject resourceObj = jsonArr.getJSONObject(n);
                String name = resourceObj.getString("name");
                int id = resourceObj.getInt("id");
                int loadType = resourceObj.getInt("loadtype");
                if (!name.equals("common.irf")){
                    mSpeakers.put(name, id);
                }
            }

            mParaCurrent.mMode = jsonObj.getInt("usermode");

        }catch (Exception e){
            Log.i(mTag, "load tts config error");
        }
        return true;
    }

    public Map<String, Integer> getSpeakers(){
        return mSpeakers;
    }
    public Map<String, Integer> getModes(){
        return mModes;
    }

    public void saveSetting(){
        mParaCurrent.mSpeakerId = mParaSetting.mSpeakerId;
        mParaCurrent.mSpeakerName = mParaSetting.mSpeakerName;
        mParaCurrent.mSpeed = mParaSetting.mSpeed;
        mParaCurrent.mVolume = mParaSetting.mVolume;
        mParaCurrent.mPitch = mParaSetting.mPitch;
        mParaCurrent.mIncrease = mParaSetting.mIncrease;
        mParaCurrent.mMode = mParaSetting.mMode;
        mParaCurrent.mModeName = mParaSetting.mModeName;
    }
}
