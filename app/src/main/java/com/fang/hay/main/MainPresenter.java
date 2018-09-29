package com.fang.hay.main;

import com.fang.hay.service.MusicPresenter;

/**
 * @author fanglh
 * @date 2018/9/28
 */
public class MainPresenter extends MusicPresenter implements MainContract.Presenter {

    MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        this.mView = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
