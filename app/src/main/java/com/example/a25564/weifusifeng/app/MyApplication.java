package com.example.a25564.weifusifeng.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 25564 on 2017/7/7.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }

}

