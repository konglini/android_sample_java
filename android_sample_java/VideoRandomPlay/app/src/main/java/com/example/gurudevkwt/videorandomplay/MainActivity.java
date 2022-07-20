package com.example.gurudevkwt.videorandomplay;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    VideoView vv_play;

    TextView tv_start;
    TextView tv_stop;

    Handler handler = new Handler();

    int video_play_time = 0;

    boolean video_stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv_play = findViewById(R.id.vv_play);
        vv_play.setVisibility(View.GONE);

        tv_start = findViewById(R.id.tv_start);
        tv_start.setOnClickListener(this);

        tv_stop = findViewById(R.id.tv_stop);
        tv_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_start) {
            if(!vv_play.isPlaying()){
                videoRandomPlay(true);
            }
        } else if(v == tv_stop){
            video_stop = true;
        }
    }

    private void videoRandomPlay(boolean first_play) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gurupet/media";

        File folder = new File(path);
        File[] list = folder.listFiles();

        ArrayList<String> file_list = new ArrayList<String>();

        String[] video = {".mp4", ".avi", ".wmv", ".mkv"};
        for (File file : list) {
            for (int i = 0; video.length > i; i++) {
                if (file.getName().endsWith(video[i])) {
                    file_list.add(file.getName());
                }
            }
        }

        if (file_list.size() <= 0) {
            return;
        }

        vv_play.requestFocus();
        vv_play.setVisibility(View.VISIBLE);
        vv_play.setVideoURI(Uri.parse(path + File.separator + file_list.get(new Random().nextInt(file_list.size()))));
        vv_play.seekTo(0);

        vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_play.start();
            }
        });
        vv_play.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoRandomPlay(false);
            }
        });

        if (first_play) {
            videoTimer();
        }
    }

    private void videoReset() {
        video_play_time = 0;
        video_stop = false;

        vv_play.stopPlayback();
        vv_play.setVisibility(View.GONE);
    }

    private void videoTimer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                video_play_time += 5000;
                Log.i("!---", video_play_time + "");

                if (video_stop) {
                    Log.i("!---", "stop");
                    videoReset();
                    return;
                } else if (video_play_time == 1 * 60 * 1000) {
                    Log.i("!---", "end");
                    videoReset();
                    return;
                }

                videoTimer();
            }
        };

        handler.postDelayed(runnable, 5000);
    }
}
