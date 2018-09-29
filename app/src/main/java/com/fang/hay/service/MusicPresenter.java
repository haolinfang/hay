package com.fang.hay.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.fang.hay.base.RxBus;
import com.fang.hay.data.event.OnServiceConnectedEvent;
import com.fang.hay.data.pojo.Music;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * @author fanglh
 * @date 2018/9/28
 */
public class MusicPresenter {

    private Player mPlayer;
    private PlayService.ProgressBinder mBinder;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (PlayService.ProgressBinder) service;
            mPlayer = mBinder.getPlayer();
            //RxBus.getInstance().send(new OnServiceConnectedEvent());
            EventBus.getDefault().post(new OnServiceConnectedEvent());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    /**
     * 绑定音乐服务
     *
     * @param context
     */
    public void bindService(Context context) {
        context.bindService(new Intent(context, PlayService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解绑音乐服务
     *
     * @param context
     */
    public void unBindService(Context context) {
        context.unbindService(mConnection);
    }

    public void play() {
        mPlayer.play();
    }

    public void pause() {
        mPlayer.pause();
    }

    public void restart() {
        mPlayer.restart();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void setPlayList(ArrayList<Music> list) {
        mPlayer.setPlayList(list);
    }

    public ArrayList<Music> getPlayList() {
        return mPlayer.getPlayList();
    }

    public void setPosition(int position) {
        mPlayer.setPosition(position);
    }

    public int getPosition() {
        return mPlayer.getPosition();
    }

    public void setProgress(int progress) {
        mPlayer.setProgress(progress);
    }

    public void playNext() {
        mPlayer.playNext();
    }

    public void playLast() {
        mPlayer.playLast();
    }

    public int getProgress() {
        return mPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mPlayer.getDuration();
    }

    public int getMusicId() {
        return mPlayer.getMusicId();
    }
}
