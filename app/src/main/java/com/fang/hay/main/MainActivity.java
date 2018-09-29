package com.fang.hay.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fang.hay.R;
import com.fang.hay.data.event.OnPlayMusicEvent;
import com.fang.hay.data.pojo.Music;
import com.fang.hay.detail.DetailActivity;
import com.fang.hay.find.FindFragment;
import com.fang.hay.local.LocalFragment;
import com.fang.hay.rec.RecFragment;
import com.fang.hay.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.iv_main_play_or_pause)
    AppCompatImageView mIvPlayOrPause;
    @BindView(R.id.tv_main_title)
    TextView mTvTitle;
    @BindView(R.id.tv_main_artist)
    TextView mTvArtist;
    @BindView(R.id.iv_main_cd)
    CircleImageView mIvCd;

    private Unbinder unbinder;
    private List<Fragment> mTabContents;
    private FragmentPagerAdapter mAdapter;
    private MainPresenter mPresenter;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    private void init() {
        mTabContents = new ArrayList<>();
        mTabContents.add(new LocalFragment());
        mTabContents.add(new RecFragment());
        mTabContents.add(new FindFragment());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }

            @Override
            public int getCount() {
                return mTabContents.size();
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2); // 预加载N界面 共启动N+1界面

        mPresenter = new MainPresenter(this);
        mPresenter.bindService(this);

        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_detail_cd);
        animation.setInterpolator(new LinearInterpolator());

//        RxBus.getInstance().toFlowable().subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                if (o instanceof OnPlayMusicEvent) {
//                    Music music = ((OnPlayMusicEvent) o).music;
//                    mTvTitle.setText(music.title);
//                    mTvArtist.setText(music.artist);
//                    mIvCd.startAnimation(animation);
//                    Glide.with(MainActivity.this).load(music.album_art).into(mIvCd);
//                }
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayMusic(OnPlayMusicEvent event) {
        mTvTitle.setText(event.music.title);
        mTvArtist.setText(event.music.artist);
        Glide.with(MainActivity.this).load(event.music.album_art).into(mIvCd);
        mIvCd.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        mPresenter.unBindService(this);
    }

    @OnClick(R.id.iv_main_play_or_pause)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_play_or_pause:
                if (mPresenter.isPlaying()) {
                    mIvPlayOrPause.setImageResource(R.drawable.vector_detail_pause);
                    mPresenter.pause();
                    mIvCd.clearAnimation();
                } else {
                    mIvPlayOrPause.setImageResource(R.drawable.vector_detail_play);
                    mPresenter.restart();
                    mIvCd.startAnimation(animation);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.ll_main_bar_bottom)
    public void bottomBarClick(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        View circle = findViewById(R.id.iv_main_cd);

        int id = SharedPreferencesUtil.getInstance().getInt(DetailActivity.PUT_SP_ID, 0);
        int position = SharedPreferencesUtil.getInstance().getInt(DetailActivity.PUT_SP_POSITION);
        int progress = SharedPreferencesUtil.getInstance().getInt(DetailActivity.PUT_SP_PROGRESS);
        String listStr = SharedPreferencesUtil.getInstance().getString(DetailActivity.PUT_SP_LIST);

        Gson gson = new Gson();
        ArrayList<Music> list = gson.fromJson(listStr, new TypeToken<ArrayList<Music>>() {
        }.getType());

        DetailActivity.activityStart(this, circle, list, 2, position, progress);
    }
}
