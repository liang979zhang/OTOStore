package com.example.administrator.otostore;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/6/10.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        myApplication=this;
    }


}
