package com.fang.hay.service;

import com.fang.hay.data.pojo.Music;

import java.util.ArrayList;

/**
 * @author fanglh
 * @date 2018/9/18
 */
public interface IPlayer {
    void play();

    void pause();

    void restart();

    void playNext();

    void playLast();

    void setProgress(int progress);

    void setPosition(int position);

    void setPlayList(ArrayList<Music> list);

    int getDuration();

    int getCurrentPosition();

    int getMusicId();

    boolean isPlaying();

    ArrayList<Music> getPlayList();

    int getPosition();
}
