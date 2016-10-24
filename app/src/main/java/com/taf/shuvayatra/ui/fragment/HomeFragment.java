package com.taf.shuvayatra.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.taf.model.Block;
import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.HomePresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.views.HomeView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements
        HomeView,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "HomeFragment";

    @Inject
    HomePresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;

    BlocksAdapter mAdapter;


    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        mAdapter = new BlocksAdapter(getContext(), getChildFragmentManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeContainer.setOnRefreshListener(this);
        mPresenter.initialize(null);
    }
    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
    }

    @Override
    public void renderBlocks(List<Block> data) {
        mAdapter.setBlocks(data);
    }

    @Override
    public void showLoadingView() {
        mSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showErrorView(String errorMessage) {
        Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }
}
