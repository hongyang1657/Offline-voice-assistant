package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;

import java.util.Map;

/**
 * 获取模型预测词典工具类
 * Created by zzy on 2018/7/17.
 */

public class DictionaryGetUtils {

    private static final String FILE_NAME_CHAR2INDEX = "char2index.json";
    private static final String FILE_NAME_INDEX2CHAR = "index2char.json";
    private static final String FILE_NAME_INDEX2INTENT = "index2intent.json";
    private static final String FILE_NAME_INTENT2INDEX = "intent2index.json";
    private static final String FILE_NAME_INDEX2SLOT = "index2slot.json";
    private static final String FILE_NAME_SLOT2INDEX = "slot2index.json";

    /**
     * 获取char2index词典
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getChar2indexMap(Context context) {
        String json = JsonResolutionUtils.getJson(context, FILE_NAME_CHAR2INDEX);
        //转换为对象
        return JsonResolutionUtils.toMap(json);
    }

    /**
     * 获取index2char词典
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getIndex2charMap(Context context) {
        String json = JsonResolutionUtils.getJson(context, FILE_NAME_INDEX2CHAR);
        //转换为对象
        return JsonResolutionUtils.toMap(json);
    }

    /**
     * 获取index2intent词典
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getIndex2intentMap(Context context) {
        String json = JsonResolutionUtils.getJson(context, FILE_NAME_INDEX2INTENT);
        //转换为对象
        return JsonResolutionUtils.toMap(json);
    }

    /**
     * 获取intent2index词典
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getIntent2indexMap(Context context) {
        String json = JsonResolutionUtils.getJson(context, FILE_NAME_INTENT2INDEX);
        //转换为对象
        return JsonResolutionUtils.toMap(json);
    }

    /**
     * 获取index2slot词典
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getIndex2slotMap(Context context) {
        String json = JsonResolutionUtils.getJson(context, FILE_NAME_INDEX2SLOT);
        //转换为对象
        return JsonResolutionUtils.toMap(json);
    }

    /**
     * 获取slot2index词典
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getSlot2indexMap(Context context) {
        String json = JsonResolutionUtils.getJson(context, FILE_NAME_SLOT2INDEX);
        //转换为对象
        return JsonResolutionUtils.toMap(json);
    }

}
