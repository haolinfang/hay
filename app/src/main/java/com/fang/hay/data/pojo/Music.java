package com.fang.hay.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author fanglh
 * @date 2018/9/27
 */
public class Music implements Parcelable {

    public int _id;
    public int _size;
    public String title;
    public int duration;
    public int artist_id;
    public String artist;
    public int album_id;
    public String album;
    public String _data;

    public String album_art; // 专辑封面

    public Music() {
    }

    protected Music(Parcel in) {
        _id = in.readInt();
        _size = in.readInt();
        title = in.readString();
        duration = in.readInt();
        artist_id = in.readInt();
        artist = in.readString();
        album_id = in.readInt();
        album = in.readString();
        album_art = in.readString();
        _data = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_size);
        dest.writeString(title);
        dest.writeInt(duration);
        dest.writeInt(artist_id);
        dest.writeString(artist);
        dest.writeInt(album_id);
        dest.writeString(album);
        dest.writeString(album_art);
        dest.writeString(_data);
    }
}
