package fitme.ai.zotyeautoassistant.utils;

public class FlightControlContants {

    public static final byte[] FRAME_0 = {(byte) 0xEE};
    public static final byte[] FRAME_1 = {(byte) 0x16};

    public static final byte[] ENTER_AIR_LINE = {0x31};  //装订航线
    public static final byte[] TAKE_OFF = {0x33};  //一键起飞
    public static final byte[] INSTRUCT_FLIGHT = {0x35};  //指令飞行
    public static final byte[] TURN_LEFT = {0x37};  //左飞
    public static final byte[] TURN_RIGHT = {0x41};  //右飞
    public static final byte[] STRAIGHT_FLIGHT = {0x39};  //直飞
    public static final byte[] CLIMB = {0x45};  //爬升
    public static final byte[] LEVEL_FLIGHT = {0x47};  //平飞
    public static final byte[] DECLINE = {0x49};  //下降
    public static final byte[] AUTO_FLIGHT = {0x53};  //自主飞行
    public static final byte[] CHECK_ENGINE = {0x55};  //查看发动机页面
    public static final byte[] RAISE_LANDING_GEAR = {0x57};  //收起落架
    public static final byte[] PUT_LANDING_GEAR = {0x59};  //放起落架
}
