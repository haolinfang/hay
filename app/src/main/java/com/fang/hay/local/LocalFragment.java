package com.fang.hay.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fang.hay.R;
import com.fang.hay.data.pojo.Music;
import com.fang.hay.detail.DetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocalFragment extends Fragment implements LocalContract.View {

    private static final int REQUEST_CODE = 1;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    Unbinder unbinder;

    private LocalContract.Presenter mPresenter;
    private LocalAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new LocalAdapter(getContext(), mItemListener);
        mPresenter = new LocalPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onLoadData(ArrayList<Music> list) {
        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();
    }

    LocalAdapter.OnItemClickListener mItemListener = new LocalAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            View view = getActivity().findViewById(R.id.iv_main_cd);
            DetailActivity.activityStart(getActivity(), REQUEST_CODE, view, mAdapter.getList(), 1, position, 0);
        }
    };
}
