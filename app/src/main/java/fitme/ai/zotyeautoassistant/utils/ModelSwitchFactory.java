package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fitme.ai.zotyeautoassistant.bean.DictionaryBean;
import fitme.ai.zotyeautoassistant.bean.Events;
import fitme.ai.zotyeautoassistant.bean.SlotReturnBean;

/**
 * Created by zzy on 2018/7/17.
 */

public class ModelSwitchFactory {

    /**
     * 此方法用于将用户请求的events转换为string序列
     *
     * @param events
     * @return
     */
    public static List<List<String>> event2string(List<Events> events) {
        List<List<String>> chars = new ArrayList<>();
        List<String> context_chars = new ArrayList<>();
        List<String> query_chars = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            if (events.size() > 1) {
                //多伦对话，带上下文，有context_chars和query_chars
                if (i < events.size() - 1) {
                    Events event = events.get(i);
//                    StringBuilder charBuilder = new StringBuilder();
                    String type = event.getType();
                    if ("u2a_speech".equals(type)) {
                        context_chars.add("[" + type + "]");
                        String speech = event.getSpeech();
                        // TODO: 2018/7/18  ...关于特殊speech扩展，暂时不做处理后期优化
                        for (char c : speech.toCharArray()) {
                            context_chars.add(c + "");
                        }
                    } else if ("a2s".equals(type)) {
                        context_chars.add("[" + type + "]");
                        String intent = event.getIntent();
                        context_chars.add("[" + intent + "]");
                        //取出request_slots,response_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>(); //使用treeMap使用默认自然排序
                        Map<String, Object> request_slots = event.getRequest_slots();
                        Map<String, Object> response_slots = event.getResponse_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        for (Map.Entry<String, Object> stringObjectEntry : response_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                context_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                context_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    context_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                context_chars.add("[sep_of_slots]");
                            }
                        }
                    } else if ("a2u_api_call".equals(type)) {
                        String intent = event.getIntent();
                        //过滤上下文中的release_turn
                        if (intent.startsWith("release_turn")) {
                            continue;
                        }
                        context_chars.add("[" + type + "]");
                        context_chars.add("[" + intent + "]");
                        //取出request_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>();
                        Map<String, Object> request_slots = event.getRequest_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                context_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                context_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    context_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                context_chars.add("[sep_of_slots]");
                            }
                        }
                    } else if ("a2u_speech".equals(type)) {
                        context_chars.add("[" + type + "]");
                        String intent = event.getIntent();
                        context_chars.add("[" + intent + "]");
                        //取出request_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>();
                        Map<String, Object> request_slots = event.getRequest_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                context_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                context_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    context_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                context_chars.add("[sep_of_slots]");
                            }
                        }
                    } else if ("event2a".equals(type)) {
                        context_chars.add("[" + type + "]");
                        String intent = event.getIntent();
                        context_chars.add("[" + intent + "]");
                        //取出request_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>();
                        Map<String, Object> request_slots = event.getRequest_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                context_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                context_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    context_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                context_chars.add("[sep_of_slots]");
                            }
                        }
                    } else {
                        Log.i("aaa", "Unknown type of event：" + event.getType());
                    }
                    context_chars.add("[end_of_event]");
                } else {
                    Events event = events.get(i);
                    String type = event.getType();
                    if ("u2a_speech".equals(type)) {
                        query_chars.add("[" + type + "]");
                        String speech = event.getSpeech();
                        // TODO: 2018/7/18  ...关于特殊speech扩展，暂时不做处理后期优化
                        for (char c : speech.toCharArray()) {
                            query_chars.add(c + "");
                        }
                    } else if ("a2s".equals(type)) {
                        query_chars.add("[" + type + "]");
                        String intent = event.getIntent();
                        query_chars.add("[" + intent + "]");
                        //取出request_slots,response_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>(); //使用treeMap使用默认自然排序
                        Map<String, Object> request_slots = event.getRequest_slots();
                        Map<String, Object> response_slots = event.getResponse_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        for (Map.Entry<String, Object> stringObjectEntry : response_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                query_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                query_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    query_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                query_chars.add("[sep_of_slots]");
                            }
                        }
                    } else if ("a2u_api_call".equals(type)) {
                        String intent = event.getIntent();
                        //过滤上下文中的release_turn
                        if (intent.startsWith("release_turn")) {
                            continue;
                        }
                        query_chars.add("[" + type + "]");
                        query_chars.add("[" + intent + "]");
                        //取出request_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>();
                        Map<String, Object> request_slots = event.getRequest_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                query_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                query_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    query_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                query_chars.add("[sep_of_slots]");
                            }
                        }
                    } else if ("a2u_speech".equals(type)) {
                        query_chars.add("[" + type + "]");
                        String intent = event.getIntent();
                        query_chars.add("[" + intent + "]");
                        //取出request_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>();
                        Map<String, Object> request_slots = event.getRequest_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                query_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                query_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    query_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                query_chars.add("[sep_of_slots]");
                            }
                        }
                    } else if ("event2a".equals(type)) {
                        query_chars.add("[" + type + "]");
                        String intent = event.getIntent();
                        query_chars.add("[" + intent + "]");
                        //取出request_slots的值重新添加到一个新的集合中
                        Map<String, Object> tmp_slots = new TreeMap<>();
                        Map<String, Object> request_slots = event.getRequest_slots();
                        for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                            String key = stringObjectEntry.getKey();
                            Object value = stringObjectEntry.getValue();
                            tmp_slots.put(key, value);
                        }
                        //遍历新的集合拼接slot_name 和slot的值
                        Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                        for (int i1 = 0; i1 < entries.size(); i1++) {
                            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next().getKey();
                                String value = (String) iterator.next().getValue();
                                query_chars.add("[" + key + "]");
                                // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                                query_chars.add("[sep_of_name_value]");
                                for (char c : value.toCharArray()) {
                                    query_chars.add(c + "");
                                }
                            }
                            if (i1 < entries.size() - 1) {
                                query_chars.add("[sep_of_slots]");
                            }
                        }
                    } else {
                        Log.i("aaa", "Unknown type of event：" + event.getType());
                    }
                    query_chars.add("[end_of_event]");
                }
            } else {
                //单轮对话不带上下文,只有query_chars
                Events event = events.get(i);
                String type = event.getType();
                if ("u2a_speech".equals(type)) {
                    query_chars.add("[" + type + "]");
                    String speech = event.getSpeech();
                    // TODO: 2018/7/18  ...关于特殊speech扩展，暂时不做处理后期优化
                    for (char c : speech.toCharArray()) {
                        query_chars.add(c + "");
                    }
                } else if ("a2s".equals(type)) {
                    query_chars.add("[" + type + "]");
                    String intent = event.getIntent();
                    query_chars.add("[" + intent + "]");
                    //取出request_slots,response_slots的值重新添加到一个新的集合中
                    Map<String, Object> tmp_slots = new TreeMap<>(); //使用treeMap使用默认自然排序
                    Map<String, Object> request_slots = event.getRequest_slots();
                    Map<String, Object> response_slots = event.getResponse_slots();
                    for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                        String key = stringObjectEntry.getKey();
                        Object value = stringObjectEntry.getValue();
                        tmp_slots.put(key, value);
                    }
                    for (Map.Entry<String, Object> stringObjectEntry : response_slots.entrySet()) {
                        String key = stringObjectEntry.getKey();
                        Object value = stringObjectEntry.getValue();
                        tmp_slots.put(key, value);
                    }
                    //遍历新的集合拼接slot_name 和slot的值
                    Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                    for (int i1 = 0; i1 < entries.size(); i1++) {
                        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next().getKey();
                            String value = (String) iterator.next().getValue();
                            query_chars.add("[" + key + "]");
                            // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                            query_chars.add("[sep_of_name_value]");
                            for (char c : value.toCharArray()) {
                                query_chars.add(c + "");
                            }
                        }
                        if (i1 < entries.size() - 1) {
                            query_chars.add("[sep_of_slots]");
                        }
                    }
                } else if ("a2u_api_call".equals(type)) {
                    String intent = event.getIntent();
                    //过滤上下文中的release_turn
                    if (intent.startsWith("release_turn")) {
                        continue;
                    }
                    query_chars.add("[" + type + "]");
                    query_chars.add("[" + intent + "]");
                    //取出request_slots的值重新添加到一个新的集合中
                    Map<String, Object> tmp_slots = new TreeMap<>();
                    Map<String, Object> request_slots = event.getRequest_slots();
                    for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                        String key = stringObjectEntry.getKey();
                        Object value = stringObjectEntry.getValue();
                        tmp_slots.put(key, value);
                    }
                    //遍历新的集合拼接slot_name 和slot的值
                    Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                    for (int i1 = 0; i1 < entries.size(); i1++) {
                        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next().getKey();
                            String value = (String) iterator.next().getValue();
                            query_chars.add("[" + key + "]");
                            // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                            query_chars.add("[sep_of_name_value]");
                            for (char c : value.toCharArray()) {
                                query_chars.add(c + "");
                            }
                        }
                        if (i1 < entries.size() - 1) {
                            query_chars.add("[sep_of_slots]");
                        }
                    }
                } else if ("a2u_speech".equals(type)) {
                    query_chars.add("[" + type + "]");
                    String intent = event.getIntent();
                    query_chars.add("[" + intent + "]");
                    //取出request_slots的值重新添加到一个新的集合中
                    Map<String, Object> tmp_slots = new TreeMap<>();
                    Map<String, Object> request_slots = event.getRequest_slots();
                    for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                        String key = stringObjectEntry.getKey();
                        Object value = stringObjectEntry.getValue();
                        tmp_slots.put(key, value);
                    }
                    //遍历新的集合拼接slot_name 和slot的值
                    Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                    for (int i1 = 0; i1 < entries.size(); i1++) {
                        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next().getKey();
                            String value = (String) iterator.next().getValue();
                            query_chars.add("[" + key + "]");
                            // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                            query_chars.add("[sep_of_name_value]");
                            for (char c : value.toCharArray()) {
                                query_chars.add(c + "");
                            }
                        }
                        if (i1 < entries.size() - 1) {
                            query_chars.add("[sep_of_slots]");
                        }
                    }
                } else if ("event2a".equals(type)) {
                    query_chars.add("[" + type + "]");
                    String intent = event.getIntent();
                    query_chars.add("[" + intent + "]");
                    //取出request_slots的值重新添加到一个新的集合中
                    Map<String, Object> tmp_slots = new TreeMap<>();
                    Map<String, Object> request_slots = event.getRequest_slots();
                    for (Map.Entry<String, Object> stringObjectEntry : request_slots.entrySet()) {
                        String key = stringObjectEntry.getKey();
                        Object value = stringObjectEntry.getValue();
                        tmp_slots.put(key, value);
                    }
                    //遍历新的集合拼接slot_name 和slot的值
                    Set<Map.Entry<String, Object>> entries = tmp_slots.entrySet();
                    for (int i1 = 0; i1 < entries.size(); i1++) {
                        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next().getKey();
                            String value = (String) iterator.next().getValue();
                            query_chars.add("[" + key + "]");
                            // TODO: 2018/7/18 特殊的slot值也就是这里的value暂时不处理，后期多伦优化
                            query_chars.add("[sep_of_name_value]");
                            for (char c : value.toCharArray()) {
                                query_chars.add(c + "");
                            }
                        }
                        if (i1 < entries.size() - 1) {
                            query_chars.add("[sep_of_slots]");
                        }
                    }
                } else {
                    Log.i("aaa", "Unknown type of event：" + event.getType());
                }
                query_chars.add("[end_of_event]");
            }
        }
        chars.add(context_chars);
        chars.add(query_chars);
        return chars;
    }


    /**
     * 从chars中获取向量序列
     *
     * @param context 上下文，用户词典获取
     * @param chars   传入的数据源
     * @return
     */
    public static List<List<Integer>> Intent2Vector(Context context, List<List<String>> chars,DictionaryBean dictionaryBean) {
        //定义返回数组
        List<List<Integer>> resultList = new ArrayList<>();
        //获取词典
        Map<String, Map<String, ?>> dictionary = dictionaryBean.getDictionary();
        Map<String, ?> intent2indexMap = dictionary.get("intent2indexMap");
        Map<String, ?> index2intentMap = dictionary.get("index2intentMap");
        Map<String, ?> slot2indexMap = dictionary.get("slot2indexMap");
        Map<String, ?> index2slotMap = dictionary.get("index2slotMap");
        Map<String, ?> char2indexMap = dictionary.get("char2indexMap");
        Map<String, ?> index2charMap = dictionary.get("index2charMap");

        //处理intent
        //获取context_chars和query_chars
        List<String> context_chars = chars.get(0);
        List<String> query_chars = chars.get(1);
        //创建返回的数组
        List<Integer> x_context = new ArrayList<>();
        List<Integer> x_query = new ArrayList<>();
        List<Integer> context_chars_seq = new ArrayList<>();
        List<Integer> query_chars_seq = new ArrayList<>();
        //处理上下文数组
        if (context_chars.size() != 0) {
            //有上下文数据
            for (String context_char : context_chars) {
                int indexFromChar = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, context_char);
                int indexFromCharDefault = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, "[unknown]");
                if (indexFromChar != -1) {
                    context_chars_seq.add(indexFromChar);
                } else {
                    context_chars_seq.add(indexFromCharDefault);
                }
            }
        }
        //处理query数组
        for (String query_char : query_chars) {
            int indexFromChar = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, query_char);
            int indexFromCharDefault = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, "[unknown]");
            if (indexFromChar != -1) {
                query_chars_seq.add(indexFromChar);
            } else {
                query_chars_seq.add(indexFromCharDefault);
            }
        }
        //获取context_chars和query_chars的真实集合长度
        int true_length_context = context_chars.size();
        int true_length_query = query_chars.size();
        //处理上下文数组大于200截取前200，不足200
        if (true_length_context > 200) {
            List<Integer> subList = context_chars_seq.subList(0, 200);
            context_chars_seq.clear();
            context_chars_seq.addAll(subList);
        } else {
            int indexFromCharPad = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, "[pad]");
            while (context_chars_seq.size() < 200) { // TODO: 2018/7/18 数组长度是否可以补齐待测试
                //真实长度如果不足200，则使用“[pad]”的index补齐
                context_chars_seq.add(0, indexFromCharPad);
            }
        }
        //处理query_chars数组大于30的截取前30，不足30的补齐
        if (true_length_query > 30) {
            List<Integer> subList = query_chars_seq.subList(0, 30);
            query_chars_seq.clear();
            query_chars_seq.addAll(subList);
        } else {
            int indexFromCharPad = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, "[pad]");
            while (query_chars_seq.size() < 30) { // TODO: 2018/7/18 数组长度是否可以补齐待测试
                //真实长度如果不足30，则使用“[pad]”的index补齐
                query_chars_seq.add(0, indexFromCharPad);
            }
        }
        //返回数据
        x_context.addAll(context_chars_seq);
        x_query.addAll(query_chars_seq);
        resultList.add(x_context);
        resultList.add(x_query);
        return resultList;
    }


    /**
     * chars转化为slots预测的向量
     *
     * @param context          上下文，用于获取词典
     * @param chars            events2chars得到的转换源数据
     * @param target_intent    第一步intent预测的intent
     * @param target_slot_name 第一步intent预测出的intent中的slots
     * @return
     */
    public static SlotReturnBean Slot2Vector(Context context, List<List<String>> chars, String target_intent, String target_slot_name, DictionaryBean dictionaryBean) {

        //定义返回的结果对象
        SlotReturnBean returnBean = new SlotReturnBean();
        //获取词典
        Map<String, Map<String, ?>> dictionary = dictionaryBean.getDictionary();
        Map<String, ?> intent2indexMap = dictionary.get("intent2indexMap");
        Map<String, ?> index2intentMap = dictionary.get("index2intentMap");
        Map<String, ?> slot2indexMap = dictionary.get("slot2indexMap");
        Map<String, ?> index2slotMap = dictionary.get("index2slotMap");
        Map<String, ?> char2indexMap = dictionary.get("char2indexMap");
        Map<String, ?> index2charMap = dictionary.get("index2charMap");

        //定义用于数据转换的数组
        List<String> baseChars = new ArrayList<>();

        //处理原始数据，将context_chars和query_chars合成一个数组
        List<String> context_chars = chars.get(0);
        List<String> query_chars = chars.get(1);
        if (context_chars.size() != 0) {
            baseChars.addAll(context_chars);
            baseChars.addAll(query_chars);
        } else {
            baseChars.addAll(query_chars);
        }

        //定义返回的数组
        List<Integer> x_passage = new ArrayList<>();
        List<Integer> x_intent = new ArrayList<>();
        List<Integer> x_slot_name = new ArrayList<>();
        List<String> new_chars = new ArrayList<>();
        List<Integer> chars_seq = new ArrayList<>();

        for (String baseChar : baseChars) {
            int indexFromChar = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, baseChar);
            int indexFromCharDefault = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, "[unknown]");
            if (indexFromChar != -1) {
                chars_seq.add(indexFromChar);
            } else {
                chars_seq.add(indexFromCharDefault);
            }
        }

        //获取chars_seq的长度,长度大于设定的最大值200则截取前200，不足200向前补齐200
        int true_length = chars_seq.size();
        if (true_length > 200) {
            List<Integer> subList = chars_seq.subList(0, 200);
            chars_seq.clear();
            chars_seq.addAll(subList);
            List<String> subListChars = baseChars.subList(0, 200);
            new_chars.addAll(subListChars);
        } else {
            int indexFromCharPad = DictionaryValueGetUtils.getIndexFromChar(char2indexMap, "[pad]");
            while (chars_seq.size() < 200){ // TODO: 2018/7/19 数组长度是否可以补齐待测试
                //真实长度如果不足200，则使用“[pad]”的index补齐
                chars_seq.add(0,indexFromCharPad);
                new_chars.addAll(baseChars);
                new_chars.add(0,"[pad]");
            }
        }

        //获取传进来的target_intent和target_slot_name的角标
        int indexFromIntent = DictionaryValueGetUtils.getIndexFromIntent(intent2indexMap, target_intent);
        int indexFromSlot = DictionaryValueGetUtils.getIndexFromSlot(slot2indexMap, target_slot_name);

        Log.i("aaa","预测到的intent和solt角标：" + target_intent + "####" + indexFromIntent + "#####" + target_slot_name + "###" + indexFromSlot);
        x_passage.addAll(chars_seq);
        x_intent.add(indexFromIntent);
        x_slot_name.add(indexFromSlot);

        returnBean.setX_passage(x_passage);
        returnBean.setX_intent(x_intent);
        returnBean.setX_slot_name(x_slot_name);
        returnBean.setNew_chars(new_chars);
        return returnBean;
    }
}
