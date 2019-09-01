package com.baroneye.DataBase;

import com.baroneye.util.Configure;
import com.baroneye.util.Utils;

import java.util.ArrayList;

class VideoClass
{
    public String url;
    int view_time;

    VideoClass(String url)
    {
        this.url = url;
        view_time = 0;
    }

}

public class DataBase {

    public ArrayList<VideoClass> shark_list;
    public int shark_current_index;
    public int shark_video_time;

    private static final DataBase ourInstance = new DataBase();

    public static DataBase getInstance() {
        return ourInstance;
    }

    private DataBase() {
        initDataBase();
    }

    private void initDataBase()
    {
        shark_current_index = Utils.load_pref_int(Configure.KEY_SHARK_INDEX, 0);
        shark_video_time = Utils.load_pref_int(Configure.KEY_SHARK_TIME, 0);

        if(shark_video_time < 0)
        {
            shark_video_time = 0;
        }

        shark_list = new ArrayList<VideoClass>();

        addList("cq_JzIjis40");
        addList("9ao5fdVgOGk");
        addList("EbPIhs0tfkU");
        addList("VXdqwkTdMTI");
        addList("AOOE8k4x9UA");


    }

    public void addList(String url)
    {
        VideoClass video = new VideoClass(url);
        shark_list.add(video);
    }

    public void plusSharkCurrentIndex()
    {
        shark_current_index++;

        if(shark_current_index >= shark_list.size())
        {
            shark_current_index = 0;
        }

        Utils.save_pref(Configure.KEY_SHARK_INDEX, shark_current_index);
    }

    public void minusSharkCurrentIndex()
    {
        shark_current_index--;

        if(shark_current_index < 0)
        {
            shark_current_index = shark_list.size() - 1;
        }

        Utils.save_pref(Configure.KEY_SHARK_INDEX, shark_current_index);
    }

    public String getUrl()
    {

        return shark_list.get(DataBase.getInstance().shark_current_index).url;
    }

}

