package com.baroneye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baroneye.R;

public class InterestYoutube extends AppCompatActivity {

    Button shark_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_youtube);

        initButton();

    }

    private void initButton()
    {
        shark_button = (Button)findViewById(R.id.button_shark);

        shark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestYoutube.this, YoutubePlayer.class);
                InterestYoutube.this.startActivity(intent);
                finish();
            }
        });
    }
}
