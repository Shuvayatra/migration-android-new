package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.FacebookActivity;
import com.taf.shuvayatra.databinding.ArticleDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.presenter.PostViewCountPresenter;
import com.taf.shuvayatra.ui.interfaces.PostDetailView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import javax.inject.Inject;

public class ArticleDetailActivity extends FacebookActivity implements PostDetailView {

    public static final String KEY_POST = "key_post";

    @Inject
    PostFavouritePresenter mFavouritePresenter;

    @Inject
    PostViewCountPresenter mPostViewCountPresenter;

    Post mPost;
    private boolean mOldFavouriteState;

    @Override
    public int getLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public boolean containsShareOption() {
        return true;
    }

    @Override
    public boolean containsFavouriteOption() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finishWithResult();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            showShareDialog(mPost);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFavouriteState() {
        UseCaseData data = new UseCaseData();
        boolean status = !(mPost.isFavourite() != null && mPost.isFavourite());
        data.putBoolean(UseCaseData.FAVOURITE_STATE, status);

        AnalyticsUtil.trackEvent(getTracker(), AnalyticsUtil.CATEGORY_FAVOURITE,
                status ? AnalyticsUtil.ACTION_LIKE : AnalyticsUtil.ACTION_UNLIKE,
                AnalyticsUtil.LABEL_ID, mPost.getId());
        mFavouritePresenter.initialize(data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(savedInstanceState != null)
                mPost = (Post) savedInstanceState.get(KEY_POST);
            else
                mPost = (Post) bundle.getSerializable(MyConstants.Extras.KEY_ARTICLE);
        }
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
        mOldFavouriteState = mPost.isFavourite() != null ? mPost.isFavourite() : false;
        Logger.e("ArticleDetailActivity", "oncreate activity:  view count: "+ mPost.getUnSyncedViewCount());
        initialize();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Logger.e("ArticleDetailActivity", "savedinstance: "+savedInstanceState);
        if(savedInstanceState==null){
            mPostViewCountPresenter.initialize(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishWithResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishWithResult() {
        Intent data = new Intent();
        Logger.e("ArticleDetailActivity", "view count: "+ mPost.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, mPost.isFavourite());
	    data.putExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, mPost.getLikes());
        data.putExtra(MyConstants.Extras.KEY_VIEW_COUNT,mPost.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_SHARE_COUNT, mPost.getUnSyncedShareCount());
        setResult(RESULT_OK, data);
        finish();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mPost.getId()))
                .build()
                .inject(this);
        mPostViewCountPresenter.attachView(this);
        mFavouritePresenter.attachView(this);
        mPostShareCountPresenter.attachView(this);
    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        boolean newFavouriteState = status ? !mOldFavouriteState : mOldFavouriteState;
        mPost.setIsFavourite(newFavouriteState);
        int likes = mPost.getLikes() == null ? 0 : mPost.getLikes();
        mPost.setLikes(newFavouriteState == mOldFavouriteState
                ? likes
                : newFavouriteState ? likes + 1 : likes - 1);
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
        mOldFavouriteState = mPost.isFavourite();
        invalidateOptionsMenu();
    }

    @Override
    public void onViewCountUpdated() {
        mPost.setUnSyncedViewCount(mPost.getUnSyncedViewCount()+1);
        Logger.e("ArticleDetailActivity", "on view updated:  view count" + mPost.getUnSyncedViewCount());
    }

    @Override
    public void onShareCountUpdate() {
        mPost.setUnSyncedShareCount(mPost.getUnSyncedShareCount()+1);
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mBinding.getRoot(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mPost != null) {
            menu.findItem(R.id.action_favourite).setIcon((mPost.isFavourite() != null && mPost
                    .isFavourite()) ? R.drawable.icon_favourite : R.drawable.icon_not_favourite);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_POST,mPost);
        super.onSaveInstanceState(outState);
    }
}
