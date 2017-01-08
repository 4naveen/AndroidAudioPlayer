package music.naveen.com.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by 2naveen on 8/28/2016.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       MainActivity mainActivity=(MainActivity)context;

        if (intent.getAction()==Intent.ACTION_HEADSET_PLUG)
        {
            Toast.makeText(mainActivity,"Headset plugin...",Toast.LENGTH_LONG).show();

        }
        context.unregisterReceiver(this);
    }
}
