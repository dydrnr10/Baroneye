package com.baroneye.DataBase;

import android.util.Log;

import com.baroneye.util.Configure;
import com.baroneye.util.Utils;

import java.util.ArrayList;
import java.util.Random;


public class RecomandDB {

    public ArrayList<String> videoNameList;
    public ArrayList<String> videoUrlList;
    public ArrayList<Integer> videoNumberList;

    public int[] recommandNumber;
    public int[] lotNumber;
    public int[] lastNumber;

    public int[] sortLotNumber;
    public int[] orderIndex;



    public RecomandDB(){
        InitDataBase();

    }

    void InitDataBase(){
        int count = 0;

        videoNameList = new ArrayList<>();
        videoUrlList = new ArrayList<>();
        videoNumberList = new ArrayList<>();

        recommandNumber = new int[4];
        lastNumber = new int[4];



        videoNameList.add("타요 띠띠뽀, 닮은 점 BEST 5 | 무엇이 무엇이 똑같을까? | 뽀로로와 타요 친구, 띠띠뽀는 누구? | 뽀요뽀요 차트쇼 | 뽀로로 랭킹쇼");
        videoUrlList.add("https://www.youtube.com/watch?v=wk-R0yEWMOA");
        videoNumberList.add(Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + count++, 0));

        videoNameList.add("뽀로로 영어동화 | 영어로 만나는 뽀로로 인어공주 이야기 | 잠자리 동화 | 뽀로로 동화");
        videoUrlList.add("https://www.youtube.com/watch?v=h0P8E_jUmCs");
        videoNumberList.add(Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + count++, 0));

        videoNameList.add("뽀로로와 바른 생활습관 기르기 | 최선을 다하기 | 뽀로로 생활습관");
        videoUrlList.add("https://www.youtube.com/watch?v=M9iW3-YrxeM");
        videoNumberList.add(Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + count++, 0));

        videoNameList.add("1화 우리는 친구 | 뽀로로와 크롱은 어떻게 처음 만나게 되었을까? | 낯선 친구와 친구되는 법 | 사회성 배우기 | 뽀로로 New1");
        videoUrlList.add("https://www.youtube.com/watch?v=DpvUZnNpm0I");
        videoNumberList.add(Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + count++, 0));

        videoNameList.add("Five Senses | 핑크퐁과 노래하며 영어로 단어를 배워요 | 워드플레이 | 영어율동동요 | 핑크퐁! 인기동요");
        videoUrlList.add("https://www.youtube.com/watch?v=-S7E1AmXjdY");
        videoNumberList.add(Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + count++, 0));

        videoNameList.add("언제 어디서나! 핑크퐁 BEST 모음 80분 | 차에서 듣는 동요 | 상어 가족 외 70곡 | + 모음집 | 핑크퐁! 인기동요");
        videoUrlList.add("https://www.youtube.com/watch?v=dBD54EZIrZo");
        videoNumberList.add(Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + count++, 0));

        videoNameList.add("도깨비 방망이 | 전래동화 | + 모음집 | 핑크퐁! 인기동화");
        videoUrlList.add("https://www.youtube.com/watch?v=8pbX_VaJU_k");
        videoNumberList.add(Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + count++, 0));

        lotNumber = new int[videoNameList.size()];

    }

    public void RecommnadShupple(){

        int maxArrNumber = videoNumberList.size();

        boolean[] numArr = new boolean[maxArrNumber];

        for(int i = 0; i < maxArrNumber; i++){
            numArr[i] = true;
        }

        Random random = new Random();
        random.nextInt();

        for(int i = 0; i < 4; i++){
            int randNum;
             while(true){
                 randNum = random.nextInt(maxArrNumber - 1);
                 if(numArr[randNum]){
                     numArr[randNum] = false;
                     break;
                 }
             }

            recommandNumber[i] = randNum;
        }

    }

    public void InitLastVideo(){

        for(int i = 0; i < 4; i++){
            lastNumber[i] = Utils.load_pref_int(Configure.KEY_LAST_VIEDO + i, -1);
        }
    }

    public void SaveLastVideoIndex(int index){

        for(int i = 3; i > 0; i--){
            if(lastNumber[i] != -1){
                Utils.save_pref(Configure.KEY_LAST_VIEDO + (i), lastNumber[i-1]);
                lastNumber[i] = lastNumber[i-1];
            }
        }

        lastNumber[0] = index;
        Utils.save_pref(Configure.KEY_LAST_VIEDO + 0, index);
    }

    public void InitLotVideo(){
        for(int i = 0; i < videoNameList.size(); i++){
            lotNumber[i] = Utils.load_pref_int(Configure.KEY_NUMBER_VIDEO + i, 0);

        }

        SortingLotVideo();
    }

    public void AddLotVideo(int index){
        lotNumber[index]++;

        Utils.save_pref(Configure.KEY_NUMBER_VIDEO + index, lotNumber[index]);
    }

    public void SortingLotVideo(){
        sortLotNumber = new int[lotNumber.length];
        orderIndex = new int[lotNumber.length];

        for(int i = 0; i < sortLotNumber.length; i++){
            sortLotNumber[i] = lotNumber[i];
            orderIndex[i] = i;
        }

        for(int i = 0; i < sortLotNumber.length; i++) {
            for(int j = i; j < sortLotNumber.length; j++){
                if(sortLotNumber[i] < sortLotNumber[j])
                {
                    int temp = sortLotNumber[i];
                    sortLotNumber[i] = sortLotNumber[j];
                    sortLotNumber[j] = temp;

                    int a = orderIndex[i];
                    int b = orderIndex[j];

                    orderIndex[i] = b;
                    orderIndex[j] = a;
                }
            }
        }

        for(int i = 0; i < lotNumber.length; i++){
            Log.d("OrderIndex", "" + orderIndex[i]);
        }

        for(int i = 0; i < lotNumber.length; i++){
            Log.d("LotIndex", "" + lotNumber[i]);
        }

        for(int i = 0; i < lotNumber.length; i++){
            Log.d("Sorting", "" + sortLotNumber[i]);
        }


    }




}
