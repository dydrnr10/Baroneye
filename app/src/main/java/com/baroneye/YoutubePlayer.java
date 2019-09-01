package com.baroneye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.baroneye.R;
import com.baroneye.DataBase.DataBase;
import com.baroneye.util.Configure;
import com.baroneye.util.Utils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YoutubePlayer extends YouTubeBaseActivity {

    YouTubePlayerView youtubeView;
    Button button;
    YouTubePlayer.OnInitializedListener listener;

    YouTubePlayer player;

    ImageButton left_button;
    ImageButton right_button;

    Context mContext;

    ImageButton home_button;

    boolean isInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("동영상을 이어서 시청하시겠습니까 ?");

        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        builder.show();
*/
        Log.d("Number", "" + DataBase.getInstance().shark_video_time);

        mContext = this;

        isInitial = false;

        button = (Button) findViewById(R.id.youtubeButton);
        youtubeView = (YouTubePlayerView)findViewById(R.id.youtubeView);
        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {

                player = youTubePlayer;

                try {
                    player.cueVideo(DataBase.getInstance().getUrl()
                            , DataBase.getInstance().shark_video_time);
                }
                catch(Exception e)
                {

                }

                Log.d("Is Sucess", "onInitial Sucess");
                isInitial = true;
            }
            @Override
            public void onInitializationFailure(
                    YouTubePlayer.Provider provider,
                    YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("Is Sucess", "onInitial Fail");
            }
        };
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Click", "onClick");
                Utils.save_pref(Configure.KEY_SHARK_TIME, 0);

                player.loadVideo(DataBase.getInstance().getUrl());
            }
        });

        left_button = (ImageButton)findViewById(R.id.button_left);
        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase.getInstance().minusSharkCurrentIndex();
                Utils.save_pref(Configure.KEY_SHARK_TIME, 0);
                player.loadVideo(DataBase.getInstance().getUrl());
            }
        });

        right_button = (ImageButton)findViewById(R.id.button_right);
        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataBase.getInstance().plusSharkCurrentIndex();
                Utils.save_pref(Configure.KEY_SHARK_TIME, 0);
                player.loadVideo(DataBase.getInstance().getUrl());
            }
        });

        home_button = (ImageButton)findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YoutubePlayer.this, StartActivity.class);
                YoutubePlayer.this.startActivity(intent);
            }
        });

        youtubeView.initialize("AIzaSyBXbzYpSlFg0BI9xEb8viOx0IvA5uqG58k", listener);


//        player.pause();
    }

    private void playYoutube()
    {

    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            Log.d("Video Time", "" + player.getCurrentTimeMillis());
            Utils.save_pref(Configure.KEY_SHARK_TIME, player.getCurrentTimeMillis());
        }
        catch (Exception e){}
    }


}
