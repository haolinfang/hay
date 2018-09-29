package com.fang.hay.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.fang.hay.data.pojo.Music;

import java.util.ArrayList;

/**
 * @author fanglh
 * @date 2018/9/26
 */
public class MediaUtil {

    private static final String BASE_URI = "content://media/external/audio/media";
    private static final String BASE_WHERE = " is_music=1 and _size>0";
    private static final String BASE_ORDER_BY = "title asc";
    private static final String[] BASE_PROJECTION = new String[]{"_id", "_size", "title", "duration", "artist_id", "artist", "album_id", "album", "_data"};

    private static final String ARTIST_URI = "content://media/external/audio/artists";
    private static final String ARTIST_ORDER_BY = "artist asc";
    private static final String[] ARTIST_PROJECTION = new String[]{"artist", "number_of_albums"};

    private static final String ALBUM_URI = "content://media/external/audio/albums";
    private static final String ALBUM_ORDER_BY = "album asc";
    private static final String[] ALBUM_PROJECTION = new String[]{"album", "album_art"};

    private static Cursor queryMusicCursor(Context context) {
        return context.getContentResolver().query(Uri.parse(BASE_URI), BASE_PROJECTION, BASE_WHERE, null, BASE_ORDER_BY);
    }

    private static Cursor queryArtistList(Context context) {
        return context.getContentResolver().query(Uri.parse(ARTIST_URI), ARTIST_PROJECTION, null, null, ARTIST_ORDER_BY);
    }

    private static Cursor queryAlbumList(Context context) {
        return context.getContentResolver().query(Uri.parse(ALBUM_URI), ALBUM_PROJECTION, null, null, ALBUM_ORDER_BY);
    }

    private static Cursor queryAlbumArtCursor(Context context, int id) {
        return context.getContentResolver().query(Uri.parse("content://media/external/audio/albums/" + id), new String[]{"album_art"}, null, null, null);
    }

    public static String queryAlbumArt(Context context, int id) {
        Cursor cursor = queryAlbumArtCursor(context, id);
        String art = null;
        if (cursor != null && cursor.moveToFirst()) {
            art = cursor.getString(0);
            cursor.close();
        }
        return art;
    }

    public static ArrayList<Music> queryMusicList(Context context) {
        ArrayList<Music> list = new ArrayList<>();
        Cursor cursor = queryMusicCursor(context);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Music music = new Music();
                music._id = cursor.getInt(cursor.getColumnIndex("_id"));
                music._size = cursor.getInt(cursor.getColumnIndex("_size"));
                music.title = cursor.getString(cursor.getColumnIndex("title"));
                music.duration = cursor.getInt(cursor.getColumnIndex("duration"));
                music.artist_id = cursor.getInt(cursor.getColumnIndex("artist_id"));
                music.artist = cursor.getString(cursor.getColumnIndex("artist"));
                music.album_id = cursor.getInt(cursor.getColumnIndex("album_id"));
                music.album = cursor.getString(cursor.getColumnIndex("album"));
                music._data = cursor.getString(cursor.getColumnIndex("_data"));
                list.add(music);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
