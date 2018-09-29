package com.fang.hay.service;

import android.media.MediaPlayer;

import com.fang.hay.base.RxBus;
import com.fang.hay.data.event.OnPlayMusicEvent;
import com.fang.hay.data.pojo.Music;

import java.io.File;
import java.util.ArrayList;

/**
 * @author fanglh
 * @date 2018/9/18
 */
public class Player implements IPlayer, MediaPlayer.OnCompletionListener {

    private static volatile Player instance;

    private MediaPlayer mMediaPlayer;
    private int mIndex = -1;
    private int mProgress = 0;
    private ArrayList<Music> mPlayList = new ArrayList<>();

    private Player() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
    }

    public static Player getInstance() {
        if (instance == null) {
            synchronized (Player.class) {
                if (instance == null) {
                    instance = new Player();
                }
            }
        }
        return instance;
    }

    @Override
    public void setPlayList(ArrayList<Music> list) {
        this.mPlayList = list;
    }

    @Override
    public void setPosition(int position) {
        this.mIndex = position;
    }

    @Override
    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    @Override
    public void play() {
        Music music = mPlayList.get(mIndex);
        if (music != null) {
            RxBus.getInstance().send(new OnPlayMusicEvent(music));

            File file = new File(music._data);
            if (!file.exists()) {
                return;
            }
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(file.getPath());
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mProgress != 0) {
                            mMediaPlayer.seekTo(mProgress);
                        }
                        mMediaPlayer.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void playNext() {
        if (mIndex != -1 && mPlayList != null && mIndex < mPlayList.size()) {
            int pos = mIndex + 1;
            if (pos >= mPlayList.size()) {
                mIndex = 0;
            } else {
                mIndex = pos;
            }
            play();
        }
    }

    @Override
    public void playLast() {
        if (mIndex != -1 && mPlayList != null && mIndex < mPlayList.size()) {
            int pos = mIndex - 1;
            if (pos == -1) {
                mIndex = mPlayList.size() - 1;
            } else {
                mIndex = pos;
            }
            play();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void restart() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playNext();
    }

    @Override
    public int getMusicId() {
        return mPlayList.get(mIndex)._id;
    }

    @Override
    public ArrayList<Music> getPlayList() {
        return mPlayList;
    }

    @Override
    public int getPosition() {
        return mIndex;
    }
}
