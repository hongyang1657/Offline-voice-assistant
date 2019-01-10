package fitme.ai.zotyeautoassistant.bean;

import java.util.Map;

/**
 * 词典
 * Created by zzy on 2018/7/23.
 */

public class DictionaryBean {
    private Map<String,Map<String, ?>> dictionary;

    public DictionaryBean() {

    }

    public DictionaryBean(Map<String, Map<String, ?>> dictionary) {
        this.dictionary = dictionary;
    }

    public Map<String, Map<String, ?>> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<String, Map<String, ?>> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public String toString() {
        return "DictionaryBean{" +
                "dictionary=" + dictionary +
                '}';
    }
}
