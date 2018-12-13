package fitme.ai.zotyeautoassistant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fitme.ai.zotyeautoassistant.MainActivity;


/**
 *
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SoundAi";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction().toString();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent startIntent = new Intent(context, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
    }
}
