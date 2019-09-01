package com.baroneye;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baroneye.util.Utils;
import com.baroneye.R;
import com.baroneye.DataBase.RecomandDB;

public class RecommandYoutube extends AppCompatActivity {

    TextView[] last_video_text;
    TextView[] lot_video_text;
    TextView[] recomand_video_text;

    RecomandDB recomandDB;

    int[] recommnadIndex;
    int[] lastIndex;
    int[] lotIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand_youtube);

        InitComponent();
        SetComponent();

    }

    void InitComponent(){
        Utils.setActivity(this);

        recomandDB = new RecomandDB();

        last_video_text = new TextView[4];
        lot_video_text = new TextView[4];
        recomand_video_text = new TextView[4];

        recommnadIndex = new int[4];
        lastIndex = new int[4];
        lotIndex = new int[4];

        last_video_text[0] = (TextView)findViewById(R.id.last_video_0);
        last_video_text[1] = (TextView)findViewById(R.id.last_video_1);
        last_video_text[2] = (TextView)findViewById(R.id.last_video_2);
        last_video_text[3] = (TextView)findViewById(R.id.last_video_3);

        lot_video_text[0] = (TextView)findViewById(R.id.lot_video_0);
        lot_video_text[1] = (TextView)findViewById(R.id.lot_video_1);
        lot_video_text[2] = (TextView)findViewById(R.id.lot_video_2);
        lot_video_text[3] = (TextView)findViewById(R.id.lot_video_3);

        recomand_video_text[0] = (TextView)findViewById(R.id.recomand_video_0);
        recomand_video_text[1] = (TextView)findViewById(R.id.recomand_video_1);
        recomand_video_text[2] = (TextView)findViewById(R.id.recomand_video_2);
        recomand_video_text[3] = (TextView)findViewById(R.id.recomand_video_3);

    }

    void SetComponent(){
        recomandDB.RecommnadShupple();
        recomandDB.InitLastVideo();
        recomandDB.InitLotVideo();

        for(int i = 0; i < 4; i++){
            recommnadIndex[i] = recomandDB.recommandNumber[i];

            String name = recomandDB.videoNameList.get(recommnadIndex[i]);
            recomand_video_text[i].setText(name);

            recomand_video_text[i].setOnClickListener(new KnowIndexOnClickListener(i) {
                @Override
                public void onClick(View view) {
                    String url = recomandDB.videoUrlList.get(recommnadIndex[index]);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));

                    startActivity(intent);

                    recomandDB.SaveLastVideoIndex(recommnadIndex[index]);
                    recomandDB.AddLotVideo(recommnadIndex[index]);
                    SetComponent();
                }
            });
        }

        for(int i = 0; i < 4; i++){
            lastIndex[i] = recomandDB.lastNumber[i];

            if(lastIndex[i] == -1){
                last_video_text[i].setText("마지막으로 본 동영상이 없습니다.");
            } else{
                last_video_text[i].setText(recomandDB.videoNameList.get(lastIndex[i]));

                last_video_text[i].setOnClickListener(new KnowIndexOnClickListener(i) {
                    @Override
                    public void onClick(View view) {
                        String url = recomandDB.videoUrlList.get(lastIndex[index]);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        startActivity(intent);

                        recomandDB.SaveLastVideoIndex(lastIndex[index]);
                        recomandDB.AddLotVideo(lastIndex[index]);

                        SetComponent();
                    }
                });
            }
        }

        for(int i = 0; i < 4; i++){
            lotIndex[i] = recomandDB.orderIndex[i];

            if(recomandDB.lotNumber[lotIndex[i]] == 0){
                lot_video_text[i].setText("많이 본 동영상이 없습니다.");
            }
            else{
                lot_video_text[i].setText(recomandDB.videoNameList.get(lotIndex[i]));
                lot_video_text[i].setOnClickListener(new KnowIndexOnClickListener(i) {
                    @Override
                    public void onClick(View view) {
                        String url = recomandDB.videoUrlList.get(lotIndex[index]);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        startActivity(intent);

                        recomandDB.SaveLastVideoIndex(lastIndex[index]);
                        recomandDB.AddLotVideo(lastIndex[index]);

                        SetComponent();
                    }
                });
            }
        }
    }

    public abstract class KnowIndexOnClickListener implements View.OnClickListener {

        protected int index;

        public KnowIndexOnClickListener(int index) {
            this.index = index;
        }
    }
}
