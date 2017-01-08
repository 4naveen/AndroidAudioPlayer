package music.naveen.com.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by 2naveen on 8/26/2016.
 */
public class MyService extends Service {
    public static MyService myService;
    MediaPlayer player=null;
    int songposition=-1;
    public void getSongPosition(int sposition)
    {
        songposition=sposition;
        try {
            player.reset();
            player.setDataSource(MainActivity.path.get(songposition));
            player.prepare();
            player.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        player=new MediaPlayer();
        myService=this;
        super.onCreate();




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         if(songposition==-1)
         {

         }
        else {
             player.start();
         }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }
    public void onPause()
    {
        player.pause();

    }
    public void next()
    {
        player.reset();
        try {
            player.setDataSource(MainActivity.path.get(songposition+1));
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.start();
    }
    public void previous()
    {
        player.reset();
        try {
            player.setDataSource(MainActivity.path.get(songposition-1));
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.start();
    }

}
