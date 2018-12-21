package fitme.ai.zotyeautoassistant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fitme.ai.zotyeautoassistant.utils.L;
import fitme.ai.zotyeautoassistant.utils.UDPSocket;

public class TestSendActivity extends Activity{

    private EditText editText;
    private Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        editText = findViewById(R.id.et_input);
        btSend = findViewById(R.id.bt_send);
        UDPSocket.getInstance();
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString().trim();
                //base64编码
                String strBase64 = Base64.encodeToString(content.getBytes(),Base64.DEFAULT);
                L.i("content:"+content+",base64编码后："+strBase64);
                UDPSocket.getInstance().sendUdpAndWaitRes(strBase64);
            }
        });
    }
}
