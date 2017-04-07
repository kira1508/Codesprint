package com.example.kkara.codesprint2;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<String> list;
    ListAdapter adapter;
    MediaPlayer mediaPlayer;
    SeekBar seek;
    Handler handler;
    Runnable runnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        handler=new Handler();
        seek=(SeekBar)findViewById(R.id.seekbar);



        Field[] fields = R.raw.class.getFields();

        for(int i=0;i<fields.length;i++){
            list.add(fields[i].getName());
            Log.d("here", String.valueOf(fields[i].getName()));
        }

        list.remove(0);
        list.remove(fields.length-2);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
             public void onItemClick(AdapterView<?> adapterView, View view,int i,long l){
                 if (mediaPlayer!=null){
                     mediaPlayer.release();
                 }
                 int resId = getResources().getIdentifier(list.get(i),"raw",getPackageName());
                 mediaPlayer = mediaPlayer.create(MainActivity.this,resId);
                 playcycle();
                 mediaPlayer.start();


                 seek.setMax(mediaPlayer.getDuration());

             }
         });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public void playcycle()
    {
        seek.setProgress(mediaPlayer.getCurrentPosition());

        if(mediaPlayer.isPlaying())
        {
            runnable=new Runnable() {
                @Override
                public void run() {
                playcycle();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }
}
