package com.taf.shuvayatra.base;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.AudioOperationsPresenter;
import com.taf.shuvayatra.presenter.PostDetailPresenter;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.presenter.PostSharePresenter;
import com.taf.shuvayatra.ui.views.PostDetailView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.taf.util.MyConstants.Extras.KEY_ID;
import static com.taf.util.MyConstants.Extras.KEY_POST;
import static com.taf.util.MyConstants.Extras.KEY_VIDEO;

public abstract class PostDetailActivity extends PlayerFragmentActivity implements PostDetailView {
    private static final String TAG = "PostDetailActivity";
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

    private boolean enableAnalytics = true;

    protected abstract void updateView(Post post);

    protected void logAnalytics(Post post) {
        AnalyticsUtil.logViewEvent(getAnalytics(), post.getId(), post.getTitle(), post.getType());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mId = bundle.getLong(KEY_ID);

        initialize();
        if (savedInstanceState != null) {
            enableAnalytics = false;
            mPost = (Post) savedInstanceState.get(KEY_POST);
            renderPost(mPost);
        } else {
            enableAnalytics = true;
            loadPost();
        }
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
            if (mPost != null && !mPost.getType().equals("audio")) share(mPost);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFavouriteState() {
        Logger.e(TAG, "update favourite state in post detail activity");
        if (mPost != null) {
            UseCaseData data = new UseCaseData();
            if (mPost.isFavourite() == null || !mPost.isFavourite()) {
                Logger.e(TAG, "favourite add");
                mPost.setIsFavourite(true);
                getPreferences().addToFavourites(mPost.getId());
            } else {
                Logger.e(TAG, "favourite remove");

                mPost.setIsFavourite(false);
                getPreferences().removeFromFavourites(mPost.getId());
            }

//            boolean status = !(mPost.isFavourite() != null && mPost.isFavourite());
            data.putBoolean(UseCaseData.FAVOURITE_STATE, mPost.isFavourite());
            data.putSerializable(UseCaseData.POST, mPost);
            AnalyticsUtil.logFavouriteEvent(getAnalytics(), mPost.getId(), mPost.getTitle(), mPost
                    .getType(), mPost.isFavourite());
            updateView(mPost);
            invalidateOptionsMenu();
            mFavouritePresenter.initialize(data);
        }
    }

    @Override
    public void renderPost(Post post) {
        Log.e(TAG, "renderPost: " + post);
        if (post != null) {
            mPost = post;
            mPost.setIsFavourite(mPreferences.isFavourite(mPost.getId()));
            mOldFavouriteState = mPost.isFavourite();
            updateView(mPost);

            if (enableAnalytics) logAnalytics(mPost);
        }
    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        //boolean newFavouriteState = status ? !mOldFavouriteState : mOldFavouriteState;
        boolean newFavouriteState = !mOldFavouriteState;
//
////        if (newFavouriteState) {
////            mPreferences.addToFavourites(mPost.getId());
////        } else {
////            mPreferences.removeFromFavourites(mPost.getId());
////        }
//        mPost.setIsFavourite(newFavouriteState);
        int likes = mPost.getLikes() == null ? 0 : mPost.getLikes();
        mPost.setLikes(newFavouriteState == mOldFavouriteState
                ? likes
                : newFavouriteState ? likes + 1 : likes - 1);
        updateView(mPost);
        mOldFavouriteState = mPost.isFavourite();
//        invalidateOptionsMenu();
    }

    @Override
    public void onViewCountUpdated() {
    }

    @Override
    public void onShareCountUpdate(boolean status) {
        if (status) {
            mPost.setShare(mPost.getShare() + 1);
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

    protected void finishWithResult() {
        Intent data = new Intent();
        if (mPost != null) {
            data.putExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, mPost.isFavourite());
            data.putExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, mPost.getLikes());
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED, data);
        }
        finish();
    }

    protected String getAMPShareURL() {
        return mPost.getShareUrl().replace("feed", "amp");
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
        AnalyticsUtil.logShareEvent(getAnalytics(), mPost.getId(), mPost.getTitle(),
                mPost.getType());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        startActivity(Utils.create(getPackageManager(), shareIntent, getString(R.string.share),
                getTargetPackagesForShare(), post));

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_POST, mPost);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPost = (Post) savedInstanceState.getSerializable(KEY_POST);
    }

    //gives list of app's package that is used to share the post
    List<String> getTargetPackagesForShare() {
        List<String> targetPackages = new ArrayList<>();

        targetPackages.add(Utils.getDefaultSmsAppPackageName(this));
        targetPackages.add("com.facebook.katana");
        targetPackages.add("com.facebook.lite");
        targetPackages.add("com.twitter.android");
        targetPackages.add("com.viber.voip");

        return targetPackages;
    }
}
