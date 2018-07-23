package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zzy on 2018/7/17.
 */


public class JsonResolutionUtils {

    /**
     * 从json文件中获取json字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获取assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 将字符串转换为对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T JsonToObject(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * 将对象转换成Json字符串
     *
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    /**
     * 将Json转换成对象
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T toEntity(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * 将Json转化成Map
     *
     * @param json
     * @return
     */
    public static Map<String, ?> toMap(String json) {
        return GSON.fromJson(json, MAP_TYPE);
    }

    /**
     * 将Json字符串转化成List
     *
     * @param json
     * @param typeOfT
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> typeOfT) {
        List<JsonObject> jsonObjectList = GSON.fromJson(json, JSON_OBJECT_TYPE);
        List<T> list = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjectList) {
            list.add(toEntity(jsonObject.toString(), typeOfT));
        }
        return list;
    }

    /**
     * Json字符串转JsonObject
     *
     * @param json
     * @return
     */
    public static JsonObject toJsonObject(String json) {
        return JSON_PARSER.parse(json).getAsJsonObject();
    }

    /**
     * 将JsonObject转换成Json字符串
     *
     * @param jsonObject
     * @return
     */
    public static String toJson(JsonObject jsonObject) {
        return jsonObject.toString();
    }

    /**
     * 禁止调用无参构造
     *
     * @throws Exception
     */
    private JsonResolutionUtils() throws Exception {
        throw new Exception("Error...");
    }

    private static final Gson GSON = new Gson();

    private static final JsonParser JSON_PARSER = new JsonParser();

    private static final Type MAP_TYPE = new TypeToken<Map<String, ?>>() {
    }.getType();

    private static final Type JSON_OBJECT_TYPE = new TypeToken<List<JsonObject>>() {
    }.getType();

}
