package fitme.ai.zotyeautoassistant.utils;


import com.iflytek.speech.NativeHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by PeiXie on 2017/3/3.
 */
public class MVWSampleParameter {
    private String mTag = "MVWSampleParameter";
    private static final String mInstanceFlag = "instance_";
    private char mSeparateWord = ',';

    class WakeupWord {
        public int threshold36 = -1;
        public int threshold40 = -1;
        public String keyWord = "";
        public int id = -1;
        public boolean absorbingWord = false;
    }

    class SceneValue{
        public int id = -1;
        public String name = "";
        public int type = -1;

        public ArrayList<WakeupWord> defWakeupWord = new ArrayList<WakeupWord>();
        public ArrayList<WakeupWord> userWakeupWord = new ArrayList<WakeupWord>();
    }

    class ParameterValue{
        String cfgPath = "";
        String resPath = "";
        NativeHandle instance = null;
        boolean active = false;
        int activeScene = 0;
        public ArrayList<SceneValue> wakeupScene = new ArrayList<SceneValue>();
    }

    private int mLang = 0;
    private int mLangSet = 0;

    private Map<String, ParameterValue> vwParameter = new HashMap<String, ParameterValue>();
    private Map<String, ParameterValue> vwParameterSet = new HashMap<String, ParameterValue>();

    public MVWSampleParameter(){}

    public ParameterValue getInstanceParameter(int n){
        return vwParameter.get(mInstanceFlag + n);
    }

    public ParameterValue getSettingInstanceParameter(int n){
        return vwParameterSet.get(mInstanceFlag + n);
    }

    public int getInstanceParameterSize(){return vwParameter.size();}
    public int getSettingInstanceParameterSize(){return vwParameterSet.size();}

    public int getLanguage(){return mLang;}
    public void setLanguage(int lang){mLang = lang;}

    public int loadConfig(ArrayList<String> lstPath){
        for (int n = 0; n != lstPath.size(); n++){
            ParameterValue param = new ParameterValue();
            vwParameter.put(mInstanceFlag + (n), param);

            //parameter set
            ParameterValue paramSet = new ParameterValue();
            vwParameterSet.put(mInstanceFlag + (n), paramSet);

            String cfgPath = lstPath.get(n).replace("/mvw/", "/Active/MVWRes/");
            switch(mLangSet){
                default:
                case 0:{
                    cfgPath += "/ResMandarin/issmvw.cfg";
                }break;
                case 1:{
                    cfgPath += "/ResEnglish/issmvw.cfg";
                }break;
            }
            param.cfgPath = cfgPath;
            param.resPath = lstPath.get(n);
            String cfgFile = new String();
            try {
                FileInputStream fin = new FileInputStream(cfgPath);
                int fileLen = fin.available();
                byte[] fileBuff = new byte[fileLen];
                fin.read(fileBuff);
                cfgFile = new String(fileBuff, "UTF-8");
            } catch (Exception e) {
                continue;
            }

            JSONTokener jTokener = new JSONTokener(cfgFile);
            try{
                JSONObject jobjCfg = (JSONObject)jTokener.nextValue();
                JSONArray jarrWUScenes = jobjCfg.getJSONArray("WakeUpScenes");

                for (int j = 0; j != jarrWUScenes.length(); j++){
                    SceneValue sceneValue = new SceneValue();
                    SceneValue sceneValueSet = new SceneValue();
                    JSONObject jobjScene = jarrWUScenes.getJSONObject(j);
                    sceneValue.type = jobjScene.getInt("KeyWordsType");
                    sceneValue.id = jobjScene.getInt("SceneId");
                    sceneValue.name = jobjScene.getString("SceneName");
					sceneValueSet.type = sceneValue.type;
                    sceneValueSet.id = sceneValue.id;
                    sceneValueSet.name = sceneValue.name;

                    JSONArray jarrDefThreshold = jobjScene.getJSONArray("KeyWordsAndDefaultThresholds");
                    for (int indexDefThreshold = 0; indexDefThreshold != jarrDefThreshold.length(); indexDefThreshold++){
                        WakeupWord wuWord = new WakeupWord();
                        WakeupWord wuWordSet = new WakeupWord();
                        JSONObject jobjWUWord = jarrDefThreshold.getJSONObject(indexDefThreshold);
                        String word = jobjWUWord.getString("KeyWord");
                        if (word == null || word.length() == 0){
                            continue;
                        }
                        wuWord.id = jobjWUWord.getInt("KeyWordId");
                        wuWord.keyWord = word;
						wuWordSet.id =wuWord.id;
                        wuWordSet.keyWord = word;
                        if (jobjWUWord.has("IsAbsorbingWord")) {
                            wuWord.absorbingWord = jobjWUWord.getInt("IsAbsorbingWord") == 1 ? true : false;
							wuWordSet.absorbingWord =wuWord.absorbingWord;
                        }
                        if (jobjWUWord.has("DefaultThreshold36")) {
                            wuWord.threshold36 = jobjWUWord.getInt("DefaultThreshold36");
							wuWordSet.threshold36 =wuWord.threshold36;
                        }
                        if (jobjWUWord.has("DefaultThreshold40")) {
                            wuWord.threshold40 = jobjWUWord.getInt("DefaultThreshold40");
							wuWordSet.threshold40 =wuWord.threshold40;
                        }

                        sceneValue.defWakeupWord.add(wuWord);
                        sceneValueSet.defWakeupWord.add(wuWordSet);
                    }

                    JSONArray jarrUserDefThreshold = jobjScene.getJSONArray("UserDefKeyWordsAndDefaultThresholds");
                    for (int indexUserDefThreshold = 0; indexUserDefThreshold != jarrUserDefThreshold.length(); indexUserDefThreshold++){
                        WakeupWord wuWord = new WakeupWord();
                        WakeupWord wuWordSet = new WakeupWord();
                        JSONObject jobjWUWord = jarrUserDefThreshold.getJSONObject(indexUserDefThreshold);
                        wuWord.id = jobjWUWord.getInt("KeyWordId");
                        wuWord.keyWord = jobjWUWord.getString("KeyWord");
						wuWordSet.id = wuWord.id;
                        wuWordSet.keyWord = wuWord.keyWord;
                        if (jobjWUWord.has("IsAbsorbingWord")) {
                            wuWord.absorbingWord = jobjWUWord.getInt("IsAbsorbingWord") == 1 ? true : false;
							 wuWordSet.absorbingWord = wuWord.absorbingWord;
                        }
                        if (jobjWUWord.has("DefaultThreshold36")) {
                            wuWord.threshold36 = jobjWUWord.getInt("DefaultThreshold36");
							wuWordSet.threshold36 = wuWord.threshold36;
                        }
                        if (jobjWUWord.has("DefaultThreshold40")) {
                            wuWord.threshold40 = jobjWUWord.getInt("DefaultThreshold40");
							wuWordSet.threshold40 = wuWord.threshold40;
                        }
                        sceneValue.userWakeupWord.add(wuWord);
                        sceneValueSet.userWakeupWord.add(wuWordSet);
                    }

                    param.wakeupScene.add(sceneValue);
                    paramSet.wakeupScene.add(sceneValueSet);
                }

            }catch (JSONException e){

            }
        }

        return vwParameter.size();
    }

	    public int reLoadConfig(ArrayList<String> lstPath){
        for (int n = 0; n != lstPath.size(); n++){
            //parameter set
            ParameterValue paramSet = new ParameterValue();
            vwParameterSet.put(mInstanceFlag + (n), paramSet);

            String cfgPath = lstPath.get(n).replace("/mvw/", "/Active/MVWRes/");
            switch(mLangSet){
                default:
                case 0:{
                    cfgPath += "/ResMandarin/issmvw.cfg";
                }break;
                case 1:{
                    cfgPath += "/ResEnglish/issmvw.cfg";
                }break;
            }
            String cfgFile = new String();
            try {
                FileInputStream fin = new FileInputStream(cfgPath);
                int fileLen = fin.available();
                byte[] fileBuff = new byte[fileLen];
                fin.read(fileBuff);
                cfgFile = new String(fileBuff, "UTF-8");
            } catch (Exception e) {
                continue;
            }

            JSONTokener jTokener = new JSONTokener(cfgFile);
            try{
                JSONObject jobjCfg = (JSONObject)jTokener.nextValue();
                JSONArray jarrWUScenes = jobjCfg.getJSONArray("WakeUpScenes");

                for (int j = 0; j != jarrWUScenes.length(); j++){
                    SceneValue sceneValueSet = new SceneValue();
                    JSONObject jobjScene = jarrWUScenes.getJSONObject(j);
                    sceneValueSet.type = jobjScene.getInt("KeyWordsType");
                    sceneValueSet.id = jobjScene.getInt("SceneId");
                    sceneValueSet.name = jobjScene.getString("SceneName");

                    JSONArray jarrDefThreshold = jobjScene.getJSONArray("KeyWordsAndDefaultThresholds");
                    for (int indexDefThreshold = 0; indexDefThreshold != jarrDefThreshold.length(); indexDefThreshold++){
                        WakeupWord wuWord = new WakeupWord();
                        JSONObject jobjWUWord = jarrDefThreshold.getJSONObject(indexDefThreshold);
                        String word = jobjWUWord.getString("KeyWord");
                        if (word == null || word.length() == 0){
                            continue;
                        }
                        wuWord.id = jobjWUWord.getInt("KeyWordId");
                        wuWord.keyWord = word;
                        if (jobjWUWord.has("IsAbsorbingWord")) {
                            wuWord.absorbingWord = jobjWUWord.getInt("IsAbsorbingWord") == 1 ? true : false;
                        }
                        if (jobjWUWord.has("DefaultThreshold36")) {
                            wuWord.threshold36 = jobjWUWord.getInt("DefaultThreshold36");
                        }
                        if (jobjWUWord.has("DefaultThreshold40")) {
                            wuWord.threshold40 = jobjWUWord.getInt("DefaultThreshold40");
                        }

                        sceneValueSet.defWakeupWord.add(wuWord);
                    }

                    JSONArray jarrUserDefThreshold = jobjScene.getJSONArray("UserDefKeyWordsAndDefaultThresholds");
                    for (int indexUserDefThreshold = 0; indexUserDefThreshold != jarrUserDefThreshold.length(); indexUserDefThreshold++){
                        WakeupWord wuWord = new WakeupWord();
                        WakeupWord wuWordSet = new WakeupWord();
                        JSONObject jobjWUWord = jarrUserDefThreshold.getJSONObject(indexUserDefThreshold);
                        wuWord.id = jobjWUWord.getInt("KeyWordId");
                        wuWord.keyWord = jobjWUWord.getString("KeyWord");
                        if (jobjWUWord.has("IsAbsorbingWord")) {
                            wuWord.absorbingWord = jobjWUWord.getInt("IsAbsorbingWord") == 1 ? true : false;
                        }
                        if (jobjWUWord.has("DefaultThreshold36")) {
                            wuWord.threshold36 = jobjWUWord.getInt("DefaultThreshold36");
                        }
                        if (jobjWUWord.has("DefaultThreshold40")) {
                            wuWord.threshold40 = jobjWUWord.getInt("DefaultThreshold40");
                        }
                        sceneValueSet.userWakeupWord.add(wuWord);
                    }
                    paramSet.wakeupScene.add(sceneValueSet);
                }

            }catch (JSONException e){

            }
        }

        return vwParameterSet.size();
    }

    public void setActiveInstance(int index, boolean active){
        vwParameterSet.get(mInstanceFlag + index).active = active;
    }

    public void setActiveScene(int instanceIndex, int sceneIndex, boolean active){
        int id = vwParameter.get(mInstanceFlag + instanceIndex).wakeupScene.get(sceneIndex).id;
        if (active){
            vwParameterSet.get(mInstanceFlag + instanceIndex).activeScene |= id;
        }else{
            vwParameterSet.get(mInstanceFlag + instanceIndex).activeScene &= (~id);
        }
    }

    public void setDefaultSceneWakeupWords(int instanceIndex, int sceneIndex){
        vwParameterSet.get(mInstanceFlag + instanceIndex).wakeupScene.get(sceneIndex).type = 0;
    }

    public boolean setUserDefineWakeupWords(int instanceIndex, int sceneIndex, String words){
        if (vwParameterSet.get(mInstanceFlag + instanceIndex) == null
                || vwParameterSet.get(mInstanceFlag + instanceIndex).wakeupScene.get(sceneIndex) == null)
            return false;

        SceneValue scene = vwParameterSet.get(mInstanceFlag + instanceIndex).wakeupScene.get(sceneIndex);
        ArrayList<WakeupWord> lst = scene.userWakeupWord;
        lst.clear();
        int l = 0; int r = words.indexOf(mSeparateWord);
        int id = 0;
        while(l>=0 && r > 0){
            String word = new String(words.substring(l, r));
            WakeupWord wakeupWord = new WakeupWord();
            wakeupWord.keyWord = word;
            wakeupWord.id = id++;
            lst.add(wakeupWord);
            l = r+1; r = words.indexOf(mSeparateWord, l);
        }
        return true;
    }

    public void setSettingLanguage(int lang){
        mLangSet = lang;
    }

    public int getSettingLanguage(){
        return mLangSet;
    }
}
