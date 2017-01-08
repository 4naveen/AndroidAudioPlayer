package music.naveen.com.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 123;
    ListView lv;
    File file;
    ArrayList<File> filelist = new ArrayList<File>();
   static ArrayList<String>path=new ArrayList<String>();
    
    LayoutInflater inflater;
    String[] files;


    android.os.Handler myhandler=new android.os.Handler();;
    SeekBar sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        sb=(SeekBar)findViewById(R.id.sb);
        Intent it=new Intent(MainActivity.this,MyService.class);
        startService(it);
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File rootfile = new File(root);

        getfile(rootfile);

        files = new String[filelist.size()];
        for (int i = 0; i < filelist.size(); i++) {
            files[i] = filelist.get(i).getName();


        }

         initNotification();
        lv.setAdapter(new Mylist());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               int songposition=position;

                if(MyService.myService!=null)
                {
                    MyService.myService.getSongPosition(songposition);
                    Intent i=new Intent(MainActivity.this,MyService.class);
                    startService(i);

                }
                for(int i=0;i<lv.getChildCount();i++)
                {
                    if(position==i)
                    {
                        lv.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        lv.getChildAt(i).animate();
                    }

                }



            }
        });

      sb.setEnabled(true);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               //  player.seekTo(i);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(new MyReceiver(),filter);
       // sb.setMax(player.getDuration());
       // updateSeekBar();
    }

   /* public void updateSeekBar(){

        sb.setProgress(player.getCurrentPosition());

        myhandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                updateSeekBar();
            }
        },10000);
    }  */


    public void getfile(File dir) {


        File listFile[] = dir.listFiles();

        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {

                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".mp3"))
                    {
                        filelist.add(listFile[i]);
                        path.add(listFile[i].getPath());
                    }
                }

            }

        }


    }


    public void doAction(View v)
    {
        switch(v.getId())
        {
            case R.id.play:
            {Intent i=new Intent(MainActivity.this,MyService.class);
                startService(i);

            }break;


            case R.id.pause:
            {
                   MyService.myService.onPause();


                }break;


            case R.id.stop:
            { Intent i=new Intent(MainActivity.this,MyService.class);
                stopService(i);

            }break;
            case R.id.back:
            {
                  MyService.myService.previous();

            }break;
            case R.id.next:
            {
                 MyService.myService.next();
            }
        }
    }
    private void initNotification() {

        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i=new Intent(MainActivity.this,MyService.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon);
        Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.icon);

        builder.setLargeIcon(bmp);
        builder.setContentTitle("MyMusicPlayer");
        builder.setContentText("Music is playing");
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setContentIntent(pi);

        Notification n = builder.build();
        nm.notify(NOTIFICATION_ID, n);
    }
    public class Mylist extends BaseAdapter
    {


        @Override
        public int getCount() {
            return files.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            inflater=getLayoutInflater();
            View v= inflater.inflate(R.layout.list_inflate,null);
            TextView tv = (TextView) v.findViewById(R.id.tv);
            tv.setText(tv.getText().toString()+files[i]);
            return v;
        }
    }
}


