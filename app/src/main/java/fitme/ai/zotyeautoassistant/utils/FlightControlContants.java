package fitme.ai.zotyeautoassistant.utils;

import java.util.HashMap;
import java.util.Map;

public class FlightControlContants {

    public static final byte[] FRAME_0 = {(byte) 0xEB};
    public static final byte[] FRAME_1 = {(byte) 0x90};

    public static final byte[] FRAME_COMMAND_0 = {(byte) 0xEE};
    public static final byte[] FRAME_COMMAND_1 = {(byte) 0x16};

    public static final byte[] START = {(byte) 0xFF};
    public static final byte[] EXIT = {(byte) 0xEE};
    public static final byte[] ENTER_AIR_LINE = {0x31};  //装订航线
    public static final byte[] TAKE_OFF = {0x33};  //一键起飞
    public static final byte[] INSTRUCT_FLIGHT = {0x35};  //指令飞行
    public static final byte[] TURN_LEFT = {0x37};  //左飞
    public static final byte[] TURN_RIGHT = {0x41};  //右飞
    public static final byte[] STRAIGHT_FLIGHT = {0x39};  //直飞
    public static final byte[] CLIMB = {0x45};  //爬升
    public static final byte[] LEVEL_FLIGHT = {0x47};  //平飞
    public static final byte[] DECLINE = {0x49};  //下降
    public static final byte[] CLOSE_ENGINE_CONSOLE = {0x51};  //关发动机页面
    public static final byte[] AUTO_FLIGHT = {0x53};  //自主飞行
    public static final byte[] CHECK_ENGINE = {0x55};  //查看发动机页面
    public static final byte[] RAISE_LANDING_GEAR = {0x57};  //收起落架
    public static final byte[] PUT_LANDING_GEAR = {0x59};  //放起落架
    public static final byte[] OPEN_CONFIRMATION = {0x61};  //打开语音确认
    public static final byte[] CLOSE_CONFIRMATION = {0x63};  //关闭语音确认

    public static final byte[] LISTENNING = {(byte) 0xAA};   //正在拾音
    public static final byte[] DISPOSING = {(byte) 0xBB};   //正在处理


    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    private static Map<String,byte[]> map = new HashMap<>();
    static {
        map.put(Constants.START_FLYING_CONTROL_SYSTEM,START);
        map.put(Constants.ENTER_AIR_LINE,ENTER_AIR_LINE);
        map.put(Constants.TAKE_OFF,TAKE_OFF);
        map.put(Constants.INSTRUCT_FLIGHT,INSTRUCT_FLIGHT);
        map.put(Constants.TURN_LEFT,TURN_LEFT);
        map.put(Constants.TURN_RIGHT,TURN_RIGHT);
        map.put(Constants.STRAIGHT_FLIGHT,STRAIGHT_FLIGHT);
        map.put(Constants.CLIMB,CLIMB);
        map.put(Constants.LEVEL_FLIGHT,LEVEL_FLIGHT);
        map.put(Constants.DECLINE,DECLINE);
        map.put(Constants.CLOSE_ENGINE_CONSOLE,CLOSE_ENGINE_CONSOLE);
        map.put(Constants.AUTO_FLIGHT,AUTO_FLIGHT);
        map.put(Constants.CHECK_ENGINE,CHECK_ENGINE);
        map.put(Constants.RAISE_LANDING_GEAR,RAISE_LANDING_GEAR);
        map.put(Constants.PUT_LANDING_GEAR,PUT_LANDING_GEAR);
        map.put(Constants.STOP_FLYING_CONTROL_SYSTEM,EXIT);
        map.put(Constants.OPEN_CONFIRMATION,OPEN_CONFIRMATION);
        map.put(Constants.CLOSE_CONFIRMATION,CLOSE_CONFIRMATION);
    }

    public static byte[] getCommandByIntent(String intent){
        return map.get(intent);
    }
}
