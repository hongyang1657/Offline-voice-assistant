package fitme.ai.zotyeautoassistant.view.impl;

public interface UdpReceiveListener {
    void onReceiver(byte[] bytes);
    void onReceiver(String msg);
}
