package com.fang.hay.detail;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.Glide;
import com.fang.hay.R;
import com.fang.hay.data.event.OnServiceConnectedEvent;
import com.fang.hay.data.pojo.Music;
import com.fang.hay.util.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity
        implements DetailContract.View, View.OnClickListener {

    public static final String GET_INTENT_FLAG = "detail_get_flag"; // 1ing 2ed
    public static final String GET_INTENT_LIST = "detail_get_list";
    public static final String GET_INTENT_POSITION = "detail_get_position";
    public static final String GET_INTENT_PROGRESS = "detail_get_progress";

    public static final String PUT_SP_ID = "detail_current_id";
    public static final String PUT_SP_LIST = "detail_current_list";
    public static final String PUT_SP_PROGRESS = "detail_current_progress";
    public static final String PUT_SP_POSITION = "detail_current_position";

    private static final long UPDATE_PROGRESS_INTERVAL = 1000;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static void activityStart(Activity activity, View view, ArrayList<Music> list, int flag, int pos, int progress) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.GET_INTENT_FLAG, flag);
        intent.putParcelableArrayListExtra(DetailActivity.GET_INTENT_LIST, list);
        intent.putExtra(DetailActivity.GET_INTENT_POSITION, pos);
        intent.putExtra(DetailActivity.GET_INTENT_PROGRESS, progress);
        intent.putExtra("transition", "circle");

        activity.startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(activity, view, "circle").toBundle());
    }

    @BindView(R.id.progressBar)
    AppCompatSeekBar mProgressBar;
    @BindView(R.id.iv_detail_mode)
    AppCompatImageView mIvMode;
    @BindView(R.id.iv_detail_last)
    AppCompatImageView mIvLast;
    @BindView(R.id.iv_detail_play_or_pause)
    AppCompatImageView mIvPlayOrPause;
    @BindView(R.id.iv_detail_next)
    AppCompatImageView mIvNext;
    @BindView(R.id.iv_detail_favorite)
    AppCompatImageView mIvFavorite;
    @BindView(R.id.iv_detail_cd)
    CircleImageView mIvCd;

    private Unbinder unbinder;
    private DetailPresenter mPresenter;
    private ArrayList<Music> mList;
    private int mPosition;
    private int mProgress;
    private int mFlag;

    private Handler mHandler = new Handler();

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isDestroyed()) {
                return;
            }
            int progress = mProgressBar.getMax() * mPresenter.getProgress() / mPresenter.getDuration();
            if (progress >= 0 && progress <= mProgressBar.getMax()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mProgressBar.setProgress(progress, true);
                } else {
                    mProgressBar.setProgress(progress);
                }
                mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_detail);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mList = getIntent().getParcelableArrayListExtra(GET_INTENT_LIST);
        mPosition = getIntent().getIntExtra(GET_INTENT_POSITION, 0);
        mProgress = getIntent().getIntExtra(GET_INTENT_PROGRESS, 0);
        mFlag = getIntent().getIntExtra(GET_INTENT_FLAG, 0);

        mPresenter = new DetailPresenter(this);
        mPresenter.bindService(this);

        mIvPlayOrPause.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
        mIvLast.setOnClickListener(this);

        Glide.with(this).load(mList.get(mPosition).album_art).into(mIvCd);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_detail_cd);
        animation.setInterpolator(new LinearInterpolator());
        mIvCd.startAnimation(animation);


//        RxBus.getInstance().toFlowable().subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object object) {
//                if (object instanceof OnServiceConnectedEvent) {
//                    onServiceConnected();
//                }
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceConnected(OnServiceConnectedEvent event) {
        if (mFlag == 1) {
            mPresenter.setPlayList(mList);
            mPresenter.setPosition(mPosition);
            mPresenter.setProgress(mProgress);
            mPresenter.play();
            mHandler.post(mRunnable);
            return;
        }
        if (mFlag == 2) {
            if (!mPresenter.isPlaying()) {
                mPresenter.setPlayList(mList);
                mPresenter.setPosition(mPosition);
                mPresenter.setProgress(mProgress);
                mPresenter.play();
            }
            mHandler.post(mRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_detail_play_or_pause:
                if (mPresenter.isPlaying()) {
                    mIvPlayOrPause.setImageResource(R.drawable.vector_detail_pause);
                    mPresenter.pause();
                } else {
                    mIvPlayOrPause.setImageResource(R.drawable.vector_detail_play);
                    mPresenter.restart();
                }
                break;
            case R.id.iv_detail_next:
                mPresenter.playNext();
                break;
            case R.id.iv_detail_last:
                mPresenter.playLast();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Gson gson = new Gson();
        SharedPreferencesUtil.getInstance().putInt(PUT_SP_ID, mPresenter.getMusicId());
        SharedPreferencesUtil.getInstance().putInt(PUT_SP_POSITION, mPresenter.getPosition());
        SharedPreferencesUtil.getInstance().putInt(PUT_SP_PROGRESS, mPresenter.getProgress());
        SharedPreferencesUtil.getInstance().putString(PUT_SP_LIST, gson.toJson(mPresenter.getPlayList()));

        finishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
