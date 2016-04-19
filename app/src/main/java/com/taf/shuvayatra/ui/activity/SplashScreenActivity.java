package com.taf.shuvayatra.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.LatestContentPresenter;
import com.taf.shuvayatra.ui.interfaces.SplashScreenView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashScreenActivity extends BaseActivity implements SplashScreenView {

    @Inject
    LatestContentPresenter mPresenter;

    @Bind(R.id.message)
    TextView messageView;

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
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule())
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    private void fetchLatestContent() {
        UseCaseData data = new UseCaseData();
        data.putLong(UseCaseData.LAST_UPDATED, getPreferences().getLastUpdateStamp());
        mPresenter.initialize(data);
    }

    @Override
    public void latestContentFetched() {
        Snackbar.make(messageView, getString(R.string.message_content_available), Snackbar
                .LENGTH_LONG).show();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                    }
                }, 1000
        );
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(messageView, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
