package fitme.ai.zotyeautoassistant.utils;

import java.util.Map;

/**
 * Created by zzy on 2018/7/17.
 */

public class DictionaryValueGetUtils {

    /**
     * 根据传入的slot获取index
     *
     * @param DictionaryMap
     * @param key
     * @return
     */
    public static int getIndexFromSlot(Map<String, ?> DictionaryMap, String key) {
        Double value = (Double) DictionaryMap.get(key);
        if (value != null) {
            return ((int) Double.parseDouble(value.toString()));
        } else {
            return -1;
        }
    }

    /**
     * 根据传入的index获取slot
     *
     * @param DictionaryMap
     * @param key
     * @return
     */
    public static String getSlotFromIndex(Map<String, ?> DictionaryMap, String key) {
        String value = (String) DictionaryMap.get(key);
        if (value != null) {
            return value;
        } else {
            return "未查到对应的slot";
        }
    }

    /**
     * 根据传入的char获取index
     *
     * @param DictionaryMap
     * @param key
     * @return
     */
    public static int getIndexFromChar(Map<String, ?> DictionaryMap, String key) {
        Double value = (Double) DictionaryMap.get(key);
        if (value != null) {
            return ((int) Double.parseDouble(value.toString()));
        } else {
            return -1;
        }
    }

    /**
     * 根据传入的index获取char
     *
     * @param DictionaryMap
     * @param key
     * @return
     */
    public static String getCharFromIndex(Map<String, ?> DictionaryMap, String key) {
        String value = (String) DictionaryMap.get(key);
        if (value != null) {
            return value;
        } else {
            return "未查到对应的char";
        }
    }

    /**
     * 根据传入的intent获取index
     *
     * @param DictionaryMap
     * @param key
     * @return
     */
    public static int getIndexFromIntent(Map<String, ?> DictionaryMap, String key) {
        Double value = (Double) DictionaryMap.get(key);
        if (value != null) {
            return ((int) Double.parseDouble(value.toString()));
        } else {
            return -1;
        }
    }

    /**
     * 根据传入的index获取对应的intent
     * @param DictionaryMap
     * @param key
     * @return
     */
    public static String getIntentFromIndex(Map<String, ?> DictionaryMap, String key) {
        String value = (String) DictionaryMap.get(key);
        if (value != null) {
            return value;
        } else {
            return "未查到对应的intent";
        }
    }
}
