package com.fang.hay.local;

import android.content.Context;

import com.fang.hay.base.BasePresenter;
import com.fang.hay.base.BaseView;
import com.fang.hay.data.pojo.Music;

import java.util.ArrayList;

/**
 * @author fanglh
 * @date 2018/9/13
 */
public interface LocalContract {

    interface View extends BaseView {
        Context getContext();

        void onLoadData(ArrayList<Music> list);
    }

    interface Presenter extends BasePresenter {
    }
}
