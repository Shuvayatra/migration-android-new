package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.NewsPresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.views.NewsView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by yipl on 1/11/17.
 */

public class NewsFragment extends BaseFragment implements NewsView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "NewsFragment";

    @Inject
    NewsPresenter mPresenter;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    @BindView(R.id.empty_view)
    View emptyView;

    BlocksAdapter mAdapter;

    public static NewsFragment getInstance() {
        return new NewsFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();

        mAdapter = new BlocksAdapter(getContext(), getChildFragmentManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(emptyView);
        mSwipeContainer.setOnRefreshListener(this);

        mPresenter.initialize(null);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void renderBlocks(List<Block> data) {
        List<BaseModel> baseModels = new ArrayList<>();
//        baseModels.addAll(BaseModel.getDummy());// TODO: 1/12/17 remove
        baseModels.addAll(data);
        mAdapter.setBlocks(baseModels);
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
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(getView(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
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
