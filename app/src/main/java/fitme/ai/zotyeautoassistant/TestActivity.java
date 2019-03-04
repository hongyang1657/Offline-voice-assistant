package fitme.ai.zotyeautoassistant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import java.util.Arrays;

import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.UDPSocket;
import fitme.ai.zotyeautoassistant.view.impl.UdpReceiveListener;

public class TestActivity extends Activity{

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        UDPSocket.getInstance();
        textView = findViewById(R.id.tv_test);
        UDPSocket.getInstance().setOnUdpReceiveListener(new UdpReceiveListener() {
            @Override
            public void onReceiver(final byte[] bytes) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(Arrays.toString(bytes));
                    }
                });
                L.i("收到的指令："+Arrays.toString(bytes));
                String str = new String(Base64.decode(bytes,Base64.DEFAULT));
                L.i("解码后："+str);
            }

            @Override
            public void onReceiver(String msg) {

            }
        });


    }
}
