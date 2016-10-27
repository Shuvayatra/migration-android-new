package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.taf.data.utils.Logger;
import com.taf.model.Block;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.JourneyPresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.views.JourneyView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class JourneyFragment extends BaseFragment implements JourneyView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "JourneyFragment";

    @Inject
    JourneyPresenter mPresenter;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    BlocksAdapter mBlocksAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_journey;
    }

    public static JourneyFragment getInstance(){
        return new JourneyFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mBlocksAdapter = new BlocksAdapter(getContext());
        mRecyclerView.setAdapter(mBlocksAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initialize(){
        DaggerDataComponent.builder()
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .dataModule(new DataModule())
                .build()
                .inject(this);

        mPresenter.attachView(this);
        mPresenter.initialize(null);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(getView(), pErrorMessage,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void renderContents(List<Block> journeyContents) {
        mBlocksAdapter.setBlocks(journeyContents);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
