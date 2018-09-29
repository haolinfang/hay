package com.fang.hay.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author fanglh
 * @date 2018/9/18
 */
public class PlayService extends Service {

    private ProgressBinder mBinder = new ProgressBinder();

    public class ProgressBinder extends Binder {
        public Player getPlayer() {
            return Player.getInstance();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("PlayService.onDestroy");
    }
}
