package com.taf.shuvayatra.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.models.Block;
import com.taf.shuvayatra.presenter.HomePresenter;
import com.taf.shuvayatra.ui.views.HomeView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements HomeView {

    public static final String TAG = "HomeFragment";

    @Inject
    HomePresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


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

    @Override
    public void renderBlocks(List<Block> data) {
        Snackbar.make(getView(), data.size() + "", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingView() {
    }

    @Override
    public void hideLoadingView() {
    }

    @Override
    public void showErrorView(String errorMessage) {
        Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }
}
