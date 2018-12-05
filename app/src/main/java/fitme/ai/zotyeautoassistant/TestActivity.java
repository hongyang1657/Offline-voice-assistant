package fitme.ai.zotyeautoassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import fitme.ai.zotyeautoassistant.utils.UDPSocket;

public class TestActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UDPSocket.getInstance();
            }
        });
    }
}
