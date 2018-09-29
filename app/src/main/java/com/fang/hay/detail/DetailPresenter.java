package com.fang.hay.detail;

import com.fang.hay.service.MusicPresenter;

/**
 * @author fanglh
 * @date 2018/9/14
 */
public class DetailPresenter extends MusicPresenter implements DetailContract.Presenter {

    private DetailContract.View mView;

    public DetailPresenter(DetailContract.View view) {
        this.mView = view;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
    }
}
