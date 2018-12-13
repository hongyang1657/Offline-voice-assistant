package fitme.ai.zotyeautoassistant.utils;

import android.content.Context;
import android.util.Log;


import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fitme.ai.zotyeautoassistant.bean.DictionaryBean;
import fitme.ai.zotyeautoassistant.bean.Events;
import fitme.ai.zotyeautoassistant.bean.ResultBean;
import fitme.ai.zotyeautoassistant.bean.SlotReturnBean;

/**
 * Created by zzy on 2018/7/19.
 */

public class ResultDealUtils {
    /**
     * 处理intent预测结果，返回对应的intent
     *
     * @param context
     * @param index
     * @return
     */
    public static String IntentDeal(Context context, String index, DictionaryBean dictionaryBean) {
        //获取词典
        Map<String, Map<String, ?>> dictionary = dictionaryBean.getDictionary();
        Map<String, ?> intent2indexMap = dictionary.get("intent2indexMap");
        Map<String, ?> index2intentMap = dictionary.get("index2intentMap");
        Map<String, ?> slot2indexMap = dictionary.get("slot2indexMap");
        Map<String, ?> index2slotMap = dictionary.get("index2slotMap");
        Map<String, ?> char2indexMap = dictionary.get("char2indexMap");
        Map<String, ?> index2charMap = dictionary.get("index2charMap");

        return DictionaryValueGetUtils.getIntentFromIndex(index2intentMap, index);
    }


    /**
     * 处理slot预测结果
     *
     * @param ps
     * @param pe
     * @param new_chars
     * @return
     */
    public static String slotDeal(int ps, int pe, List<String> new_chars) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < new_chars.size(); i++) {
            if (i >= ps && i <= pe) {
                builder.append(new_chars.get(i));
            }
        }
        return builder.toString();
    }

    /**
     * 获取Context_Pe
     *
     * @param context
     * @return
     */
    public static float[] getContextPe(Context context) {
        int tmpContext = 0;
        float[] context_pe = new float[60000];
        List<List<String>> stringList = new ArrayList<>();
        try {
            InputStream path = context.getClass().getResourceAsStream("/assets/context_pe.txt");
            DataInputStream dataInputStream = new DataInputStream(path);
            String len;
            while ((len = dataInputStream.readLine()) != null) {
                List<String> tempList = new ArrayList<String>();
                tempList.add(len);
                stringList.add(tempList);
            }
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //处理数组
        for (int i = 0; i < stringList.size(); i++) {
            List<String> list = stringList.get(i);
            String[] split = list.get(0).split("\\t");
            for (int j = 0; j < split.length; j++) {
                context_pe[tmpContext] = Float.parseFloat(split[j]);
                tmpContext++;
            }
        }
        return context_pe;
    }


    /**
     * 获取Query_tPe
     *
     * @param context
     * @return
     */
    public static float[] getQuery_tPe(Context context) {
        int tmpQuery = 0;
        float[] query_pe = new float[9000];
        List<List<String>> stringList = new ArrayList<>();
        try {
            InputStream path = context.getClass().getResourceAsStream("/assets/query_pe.txt");
            DataInputStream dataInputStream = new DataInputStream(path);
            String len;
            while ((len = dataInputStream.readLine()) != null) {
                List<String> tempList = new ArrayList<String>();
                tempList.add(len);
                stringList.add(tempList);
            }
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //处理数组
        for (int i = 0; i < stringList.size(); i++) {
            List<String> list = stringList.get(i);
            String[] split = list.get(0).split("\\t");
            for (int j = 0; j < split.length; j++) {
                query_pe[tmpQuery] = Float.parseFloat(split[j]);
                tmpQuery++;
            }
        }
        return query_pe;
    }

    /**
     * 模型预测的方法
     *
     * @param ct                        上下文context
     * @param requestString             用户请求的speech
     * @param type                      请求的消息类型
     * @param message_id                用户创建的message_id,返回的时候为from_message_id
     * @param context_Pe                上下文向量，在程序加载时候做一次初始化
     * @param query_Pe                  请求体的向量，在程序加载的时候做一次初始化
     * @param tensorflowInterfaceIntent intent预测的tensorflow对象
     * @param tensorflowInterfaceSlot   slot预测的tensorflow对象
     * @return
     */
    public static ResultBean modelForecast(Context ct, String requestString, String type, String message_id, float[] context_Pe, float[] query_Pe, TensorFlowInferenceInterface tensorflowInterfaceIntent, TensorFlowInferenceInterface tensorflowInterfaceSlot, DictionaryBean dictionaryBean) {
        //定义返回的数据
        ResultBean resultBean = new ResultBean();
        List<String> slot = new ArrayList<>();

        //本地封装请求语句
        List<Events> eventses = new ArrayList<>();
        eventses.add(new Events(type, requestString, null, null, null));
        List<List<String>> chars = ModelSwitchFactory.event2string(eventses);
        Log.i("aaa", chars.toString() + "长度：" + chars.size());
        //构建intent预测所需参数
        List<List<Integer>> intent2VectorResult = ModelSwitchFactory.Intent2Vector(ct, chars, dictionaryBean);
        List<Integer> x_context = intent2VectorResult.get(0);
        List<Integer> x_query = intent2VectorResult.get(1);
        Log.i("aaa", x_context.toString() + "长度：" + x_context.size());
        Log.i("aaa", x_query.toString() + "长度：" + x_query.size());

        //转换list<>类型的集合未int[]
        int[] context = new int[200];
        int[] query = new int[30];
        for (int i = 0; i < x_context.size(); i++) {
            context[i] = x_context.get(i);
        }
        for (int i = 0; i < x_query.size(); i++) {
            query[i] = x_query.get(i);
        }
        //获取聊天分数预测的数组长度
        int intent_score_size = dictionaryBean.getDictionary().get("intent2indexMap").size();
        //定义输出结果
        long[] predict_intent = new long[1];
        float[] intent_score = new float[intent_score_size];
        Log.i("aaa", "---------------------------------------------------开始调用intent.pb模型-----------------------------------------------");
        //调用intent模型
        tensorflowInterfaceIntent.feed("model_intent/context", context, new long[]{1, 200});
        tensorflowInterfaceIntent.feed("model_intent/query", query, new long[]{1, 30});
        tensorflowInterfaceIntent.feed("model_intent/context_pe", context_Pe, new long[]{200, 300});
        tensorflowInterfaceIntent.feed("model_intent/query_pe", query_Pe, new long[]{30, 300});
//        tensorflowInterfaceIntent.run(new String[]{"model_intent/predict_intent"});
        tensorflowInterfaceIntent.run(new String[]{"model_intent/predict_intent","model_intent/intent_score"});
        tensorflowInterfaceIntent.fetch("model_intent/predict_intent", predict_intent);
        tensorflowInterfaceIntent.fetch("model_intent/intent_score", intent_score);
        Log.i("aaa", "---------------------------------------------------intent.pb模型预测结束-----------------------------------------------");
        for (long f1 : predict_intent) {
            Log.i("aaa", "intent预测结果：" + f1);
        }

        for (float f2 : intent_score) {
            Log.i("aaa","intent预测到的聊天分数：" + f2);
        }

        //处理intent结果，得到Solt的请求的参数
        String intentIndex = String.valueOf(predict_intent[0]);
        String intentDeal = ResultDealUtils.IntentDeal(ct, intentIndex, dictionaryBean);
        Log.i("aaa", "得到的intent：" + intentDeal);
        if (!"未查到对应的intent".equals(intentDeal)) {
            String[] split = intentDeal.replaceAll("\\<", "-").replaceAll("\\>", "-").split("-");
            Log.i("aaa", "split:" + Arrays.toString(split));
            if (split.length > 1) {
                for (int i = 0; i < split.length; i++) {
                    if (i > 0 && i < split.length) {
                        if ("".equals(split[i])) {
                            continue;//去除数组中的空串
                        }
                        //构建solt预测所需参数
                        SlotReturnBean slotReturnBean = ModelSwitchFactory.Slot2Vector(ct, chars, intentDeal, split[i], dictionaryBean);
                        List<Integer> x_passage = slotReturnBean.getX_passage();
                        List<Integer> x_intent = slotReturnBean.getX_intent();
                        List<Integer> x_slot_name = slotReturnBean.getX_slot_name();
                        List<String> new_chars = slotReturnBean.getNew_chars();
                        //处理得到的参数
                        int[] passage = new int[200];
                        int[] intent = new int[1];
                        int[] slot_name = new int[1];
                        for (int j = 0; j < x_passage.size(); j++) {
                            passage[j] = x_passage.get(j);
                        }
                        for (int j = 0; j < x_intent.size(); j++) {
                            intent[j] = x_intent.get(j);
                        }
                        for (int j = 0; j < x_slot_name.size(); j++) {
                            slot_name[j] = x_slot_name.get(j);
                        }
                        Log.i("aaa", "passage:#" + Arrays.toString(passage) + "passage长度：" + passage.length);
                        Log.i("aaa", "intent长度：" + intent.length);
                        Log.i("aaa", "slot_name长度：" + slot_name.length);
                        //定义输出结果
                        long[] result_ps = new long[1];
                        long[] result_pe = new long[1];
                        //开始预测slot
                        Log.i("aaa", "---------------------------------------------------开始调用slots.pb模型-----------------------------------------------");
                        tensorflowInterfaceSlot.feed("model_slot/passage", passage, new long[]{1, 200});
                        tensorflowInterfaceSlot.feed("model_slot/intent", intent, new long[]{1});
                        tensorflowInterfaceSlot.feed("model_slot/slot_name", slot_name, new long[]{1});
                        tensorflowInterfaceSlot.run(new String[]{"model_slot/predict_ps", "model_slot/predict_pe"});
                        tensorflowInterfaceSlot.fetch("model_slot/predict_ps", result_ps);
                        tensorflowInterfaceSlot.fetch("model_slot/predict_pe", result_pe);
                        Log.i("aaa", "---------------------------------------------------slots.pb模型结束-----------------------------------------------");

                        for (long f1 : result_ps) {
                            Log.i("aaa", "验证结果ps：" + f1);
                        }

                        for (Long f2 : result_pe) {
                            Log.i("aaa", "验证结果pe：" + f2);
                        }
                        Log.i("aaa", "new_chars：" + new_chars.size());
                        //处理solot结果
                        String slotDeal = ResultDealUtils.slotDeal((int) result_ps[0], (int) result_pe[0], new_chars);
                        Log.i("aaa", "最终预测的intent" + intentDeal);
                        Log.i("aaa", "最终预测的solots" + slotDeal);

                        slot.add(slotDeal);
                    }
                }
                resultBean.setState("success");
                resultBean.setFrom_message_id(message_id);
                resultBean.setIntent(intentDeal);
                resultBean.setSlot(slot);
                resultBean.setIntent_score(intent_score[(int) predict_intent[0]]);//intent预测分数
                return resultBean;
            } else {
                //没有slot
                Log.i("aaa", "slot为空直接返回intent和空slot");
                resultBean.setState("success");
                resultBean.setFrom_message_id(message_id);
                resultBean.setIntent(intentDeal);
                resultBean.setSlot(slot);
                resultBean.setIntent_score(intent_score[(int) predict_intent[0]]);//intent预测分数
                return resultBean;
            }
        } else {
            Log.i("aaa", "未查到对应的intent");
            resultBean.setState("未查到对应的intent");
            resultBean.setFrom_message_id(message_id);
            resultBean.setIntent(intentDeal);
            resultBean.setSlot(slot);
            resultBean.setIntent_score(intent_score[(int) predict_intent[0]]);//intent预测分数
            return resultBean;
        }
    }
}
