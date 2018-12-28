package fitme.ai.zotyeautoassistant.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import fitme.ai.zotyeautoassistant.view.impl.UdpReceiveListener;

/**
 * 飞控操作命令组播
 */
public class UDPSocketRec {

    private static final int DEFAULT_PORT = 18003;//端口号57816  18001
    private static final int MAX_DATA_PACKET_LENGTH = 256;
    private static final String HOST = "226.0.0.22";
    //private static final String HOST = "255.255.255.255";

    private RecThread recThread = null;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                if (recThread!=null){
                    recThread.interrupt();
                }
                recThread = null;
                recThread = new RecThread();
                recThread.start();
            }
        }
    };

    private MulticastSocket mSocket;
    public UDPSocketRec(){
        if (mSocket==null){
            try {
                mSocket = new MulticastSocket(DEFAULT_PORT);
                //mSocket.setReuseAddress(true);
                //mSocket.bind(new InetSocketAddress(InetAddress.getByName(HOST),DEFAULT_PORT));
                mSocket.joinGroup(InetAddress.getByName(HOST));
                //mSocket.bind(new InetSocketAddress(DEFAULT_PORT));
                //开启接收线程
                recThread = new RecThread();
                recThread.start();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static UDPSocketRec udpSocket;
    public static UDPSocketRec getInstance(){
        if (udpSocket==null){
            udpSocket = new UDPSocketRec();
        }
        return udpSocket;
    }




    private class RecThread extends Thread{

        @Override
        public void run() {
            super.run();
            if (mSocket == null || mSocket.isClosed())
                return;
            try {
                byte datas[] = new byte[MAX_DATA_PACKET_LENGTH];
                InetAddress address = InetAddress.getByName(HOST);
                DatagramPacket packet = new DatagramPacket(datas, datas.length,address,DEFAULT_PORT);
                mSocket.receive(packet);
                String receiveMsg = new String(packet.getData()).trim();
                Log.i("debug_message", "--------------run收到tts: "+receiveMsg);
                if (listener!=null){
                    listener.onReceiver(receiveMsg);
                    listener.onReceiver(packet.getData());
                }
                mHandler.sendEmptyMessage(2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private UdpReceiveListener listener;
    public void setOnUdpReceiveListener(UdpReceiveListener listener){
        this.listener = listener;
    }
}
