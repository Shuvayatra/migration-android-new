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
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.AudioOperationsPresenter;
import com.taf.shuvayatra.presenter.PostDetailPresenter;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.presenter.PostSharePresenter;
import com.taf.shuvayatra.ui.views.PostDetailView;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import static com.taf.util.MyConstants.Extras.KEY_ID;
import static com.taf.util.MyConstants.Extras.KEY_POST;

public abstract class PostDetailActivity extends BaseActivity implements PostDetailView {

    public Long mId;
    @Inject
    protected PostDetailPresenter mDetailPresenter;
    @Inject
    protected PostFavouritePresenter mFavouritePresenter;
    @Inject
    protected AudioOperationsPresenter mPresenter;
    @Inject
    protected PostSharePresenter mSharePresenter;

    protected Post mPost;
    private boolean mOldFavouriteState;

    protected abstract void updateView(Post post);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mId = bundle.getLong(KEY_ID);

        initialize();
        loadPost();
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
            if (!mPost.getType().equals("audio")) share(mPost);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFavouriteState() {
        UseCaseData data = new UseCaseData();
        boolean status = !(mPost.isFavourite() != null && mPost.isFavourite());
        data.putBoolean(UseCaseData.FAVOURITE_STATE, status);

        mFavouritePresenter.initialize(data);
    }

    @Override
    public void renderPost(Post post) {
        if (post != null) {
            mPost = post;
            mPost.setIsFavourite(mPreferences.isFavourite(mPost.getId()));
            mOldFavouriteState = mPost.isFavourite();
            updateView(mPost);
        }
    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        //boolean newFavouriteState = status ? !mOldFavouriteState : mOldFavouriteState;
        boolean newFavouriteState = !mOldFavouriteState;

        if (newFavouriteState) {
            mPreferences.addToFavourites(mPost.getId());
        } else {
            mPreferences.removeFromFavourites(mPost.getId());
        }
        mPost.setIsFavourite(newFavouriteState);
        int likes = mPost.getLikes() == null ? 0 : mPost.getLikes();
        mPost.setLikes(newFavouriteState == mOldFavouriteState
                ? likes
                : newFavouriteState ? likes + 1 : likes - 1);
        updateView(mPost);
        mOldFavouriteState = mPost.isFavourite();
        invalidateOptionsMenu();
    }

    @Override
    public void onViewCountUpdated() {
    }

    @Override
    public void onShareCountUpdate(boolean status) {
        if (status) {
            mPost.setUnSyncedShareCount(mPost.getUnSyncedShareCount() + 1);
            updateView(mPost);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishWithResult();
        }
        return super.onKeyDown(keyCode, event);
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

    private void finishWithResult() {
        Intent data = new Intent();
        Logger.e("ArticleDetailActivity", "view count: " + mPost.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, mPost.isFavourite());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, mPost.getLikes());
        data.putExtra(MyConstants.Extras.KEY_VIEW_COUNT, mPost.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_SHARE_COUNT, mPost.getUnSyncedShareCount());
        setResult(RESULT_OK, data);
        finish();
    }

    protected void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mId))
                .build().inject(this);

        mDetailPresenter.attachView(this);
        mFavouritePresenter.attachView(this);
        mSharePresenter.attachView(this);
    }

    protected void loadPost() {
        mDetailPresenter.initialize(null);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mBinding.getRoot(), pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    public boolean share(final Post post) {
        mSharePresenter.initialize(null);
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, post.getShareUrl());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_POST, mPost);
        super.onSaveInstanceState(outState);
    }
}
