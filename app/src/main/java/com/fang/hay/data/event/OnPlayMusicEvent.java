package com.fang.hay.data.event;

import com.fang.hay.data.pojo.Music;

/**
 * @author fanglh
 * @date 2018/9/19
 */
public class OnPlayMusicEvent {

    public Music music;

    public OnPlayMusicEvent(Music music) {
        this.music = music;
    }
}
