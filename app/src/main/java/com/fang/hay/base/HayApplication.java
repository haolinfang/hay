package com.fang.hay.base;

import android.app.Application;
import android.content.Context;

import com.fang.hay.util.SharedPreferencesUtil;

/**
 * @author fanglh
 * @date 2018/9/28
 */
public class HayApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }
}
