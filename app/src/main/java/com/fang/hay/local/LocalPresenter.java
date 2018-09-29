package com.fang.hay.local;

import com.fang.hay.data.pojo.Music;
import com.fang.hay.util.MediaUtil;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author fanglh
 * @date 2018/9/13
 */
public class LocalPresenter implements LocalContract.Presenter {

    LocalContract.View mView;

    public LocalPresenter() {
    }

    public LocalPresenter(LocalContract.View view) {
        this.mView = view;
    }

    @Override
    public void subscribe() {
        loadData();
    }

    @Override
    public void unSubscribe() {

    }

    /**
     * 查询音乐
     */
    private void loadData() {
        Observable.create(new ObservableOnSubscribe<ArrayList<Music>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<Music>> emitter) throws Exception {
                ArrayList<Music> list = MediaUtil.queryMusicList(mView.getContext());
                if (null != list && list.size() > 0) {
                    for (Music music : list) {
                        music.album_art = MediaUtil.queryAlbumArt(mView.getContext(), music.album_id);
                    }
                }
                emitter.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Music>>() {
                    @Override
                    public void accept(ArrayList<Music> music) throws Exception {
                        mView.onLoadData(music);
                    }
                });
    }
}
