package com.taf.shuvayatra.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.LatestContentPresenter;
import com.taf.shuvayatra.presenter.SyncFavouritesPresenter;
import com.taf.shuvayatra.ui.interfaces.SplashScreenView;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashScreenActivity extends BaseActivity implements
        SplashScreenView {

    @Inject
    LatestContentPresenter mPresenter;
    @Inject
    SyncFavouritesPresenter mSyncPresenter;


    @Bind(R.id.message)
    TextView messageView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    UseCaseData mUseCaseData = new UseCaseData();

    @Override
    public int getLayout() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");

        initialize();
        fetchLatestContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule(false, true))
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mSyncPresenter.attachView(this);
    }

    private void fetchLatestContent() {
        messageView.setText(getString(R.string.message_fetching_latest_content));
        UseCaseData data = new UseCaseData();
        data.putLong(UseCaseData.LAST_UPDATED, getPreferences().getLastUpdateStamp());
        mPresenter.initialize(data);
    }

    private void syncFavourites(List<Post> pUnSyncedPosts) {
        messageView.setText(getString(R.string.message_syncing_favourites));

        mUseCaseData.clearAll();
        mUseCaseData.putBoolean(UseCaseData.SEARCH_UN_SYNCED_DATA, false);
        mUseCaseData.putSerializable(UseCaseData.SYNC_LIST, (Serializable) pUnSyncedPosts);
        mSyncPresenter.initialize(mUseCaseData);
    }

    private void searchUnSyncedFavourites() {
        messageView.setText(getString(R.string.message_search_un_synced_favourites));

        mUseCaseData.clearAll();
        mUseCaseData.putBoolean(UseCaseData.SEARCH_UN_SYNCED_DATA, true);
        mSyncPresenter.initialize(mUseCaseData);
    }

    @Override
    public void latestContentFetched(boolean hasNewContents) {
        if (hasNewContents)
            messageView.setText(getString(R.string.message_content_available));

        searchUnSyncedFavourites();
    }

    @Override
    public void unSyncedFavouritesSearched(List<Post> pUnSyncedPosts) {
        if (pUnSyncedPosts != null && !pUnSyncedPosts.isEmpty()) {
            syncFavourites(pUnSyncedPosts);
        } else {
            messageView.setText(getString(R.string.message_no_un_synced_favourites));
            exitSplashScreen();
        }
    }

    @Override
    public void favouritesSynced(Boolean status) {
        messageView.setText(getString(status ? R.string.message_favourites_synced : R.string
                .message_not_all_favourites_synced));
        exitSplashScreen();
    }

    @Override
    public void showLoadingView() {
        mProgressBar.setVisibility(View.VISIBLE);
        //messageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mProgressBar.setVisibility(View.GONE);
        //messageView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(messageView, pErrorMessage, Snackbar.LENGTH_LONG).show();
        exitSplashScreen();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void exitSplashScreen() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 400
        );
    }
}
