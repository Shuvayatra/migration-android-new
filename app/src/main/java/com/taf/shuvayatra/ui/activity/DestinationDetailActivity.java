package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PlayerFragmentActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.DestinationBlocksPresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.views.DestinationDetailView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DestinationDetailActivity extends PlayerFragmentActivity implements
        DestinationDetailView,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "DestinationDetailActivity";

    @Inject
    DestinationBlocksPresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    BlocksAdapter mAdapter;
    Country mCountry;

    @Override
    public int getLayout() {
        return R.layout.activity_destination_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG, "oncreate called");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Logger.e(TAG, ": " + bundle.containsKey(MyConstants.Extras.KEY_COUNTRY));
            mCountry = (Country) bundle.get(MyConstants.Extras.KEY_COUNTRY);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mCountry.getTitle());
        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .dataModule(new DataModule(mCountry.getId()))
                .build()
                .inject(this);

        mPresenter.attachView(this);
        mAdapter = new BlocksAdapter(this);
        List<BaseModel> initList = new ArrayList<>();
        initList.add(mCountry);
        mAdapter.setBlocks(initList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mPresenter.initialize(null);

    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void renderBlocks(List<Block> blocks) {
        List<BaseModel> models = new ArrayList<>();
        if (blocks.isEmpty()) {
            models.add(mCountry);
        } else {
            models.addAll(blocks);
            if (blocks.get(0).getLayout().equalsIgnoreCase("notice")) {
                models.add(1, mCountry);
            } else {
                models.add(0, mCountry);
            }
        }
        mAdapter.setBlocks(models);
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
}
