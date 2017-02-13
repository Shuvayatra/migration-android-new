package com.taf.shuvayatra.ui.deprecated.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.deprecated.SinglePostPresenter;
import com.taf.shuvayatra.ui.deprecated.interfaces.PostView;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import butterknife.BindView;

@Deprecated
public class FacebookParseActivity extends BaseActivity implements PostView {

    Long mId;
    @Inject
    SinglePostPresenter mSinglePostPresenter;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressbar;

    @Override
    public int getLayout() {
        return R.layout.activity_facebook_parse;
    }

    @Override
    public String screenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        Logger.e("FacebookParseActivity", "target url: "+ targetUrl.toString());

        if (targetUrl != null) {
            Logger.e("FacebookParseActivity", "query parmeter: "+ targetUrl.getQuery());
            mId = Long.valueOf(targetUrl.getQueryParameter("id"));
            Logger.e("FacebookParseActivity", "App Link Target URL: " + targetUrl.getQueryParameter("id"));
        }*/
        initialize();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mId))
                .build()
                .inject(this);
        mSinglePostPresenter.attachView(this);
        mSinglePostPresenter.initialize(null);
    }

    @Override
    public void showErrorView(String pErrorMessage) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void postLoadCompleted(Post pPost) {
        Logger.e("FacebookParseActivity", "post from fb: "+pPost);
        if(pPost!=null){
            Intent intent;
            switch (pPost.getDataType()){
                case MyConstants.Adapter.TYPE_TEXT:
                case MyConstants.Adapter.TYPE_NEWS:
                    intent = new Intent(this, ArticleDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_ARTICLE,pPost);
                    startActivity(intent);
                    break;
                case MyConstants.Adapter.TYPE_AUDIO:
                    intent = new Intent(this, AudioDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_AUDIO,pPost);
                    startActivity(intent);
                    break;
                case MyConstants.Adapter.TYPE_VIDEO:
                    intent = new Intent(this, VideoDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_VIDEO,pPost);
                    startActivity(intent);
                    break;
                case MyConstants.Adapter.TYPE_PLACE:
                    intent = new Intent(this,PlacesDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_PLACE, pPost);
                    startActivity(intent);
                    break;
            }
            finish();
        }
        else{
            Logger.e("FacebookParseActivity", "new post null: "+ pPost);
            Snackbar.make(mProgressbar,"Please sync with latest content",Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoadingView() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
