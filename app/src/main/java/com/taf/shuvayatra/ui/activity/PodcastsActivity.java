package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.taf.model.BaseModel;
import com.taf.model.Podcast;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PlayerFragmentActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PodcastListPresenter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.fragment.MiniPlayerFragment;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.PodcastListView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PodcastsActivity extends PlayerFragmentActivity implements
        PodcastListView,
        ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    PodcastListPresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;

    ListAdapter<Podcast> mAdapter;

    Long mId;
    String mTitle;

    @Override
    public int getLayout() {
        return R.layout.activity_podcasts;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();
        mId = data.getLong(MyConstants.Extras.KEY_ID, -1);
        mTitle = data.getString(MyConstants.Extras.KEY_TITLE, "");

        getSupportActionBar().setTitle(mTitle);
        if (savedInstanceState != null) {
            AnalyticsUtil.logViewEvent(getAnalytics(), mId, mTitle, "podcast-channel");
        }

        initialize();

        mAdapter = new ListAdapter(getContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeContainer.setOnRefreshListener(this);

        mPresenter.initialize(null);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_player, new MiniPlayerFragment(), MiniPlayerFragment.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean alwaysShowPlayer() {
        return true;
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {

    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
    }

    @Override
    public void renderPodcasts(List<Podcast> podcasts) {
        mAdapter.setDataCollection(podcasts);
        ((MyApplication) getApplicationContext()).mService.setPodcasts(podcasts);
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
        Snackbar.make(mSwipeContainer, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mId))
                .build()
                .inject(this);

        mPresenter.attachView(this);
    }
}
