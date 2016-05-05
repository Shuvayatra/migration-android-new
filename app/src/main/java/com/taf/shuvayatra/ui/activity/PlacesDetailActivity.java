package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.FacebookActivity;
import com.taf.shuvayatra.databinding.PlaceDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PlacesListPresenter;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.ui.interfaces.PlacesListView;
import com.taf.shuvayatra.ui.interfaces.PostDetailView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class PlacesDetailActivity extends FacebookActivity implements PlacesListView, PostDetailView {

    @Inject
    PlacesListPresenter mPresenter;
    @Inject
    PostFavouritePresenter mFavouritePresenter;

    @Bind(R.id.places_container)
    LinearLayout mPlacesContainer;

    Post mPlace;
    boolean mOldFavouriteState;

    @Override
    public int getLayout() {
        return R.layout.activity_places_detail;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishWithResult();
                break;
            case R.id.action_share:
                showShareDialog(mPlace);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFavouriteState() {
        UseCaseData data = new UseCaseData();
        boolean status = !(mPlace.isFavourite() != null && mPlace.isFavourite());
        data.putBoolean(UseCaseData.FAVOURITE_STATE, status);

        AnalyticsUtil.trackEvent(getTracker(), AnalyticsUtil.CATEGORY_FAVOURITE,
                status ? AnalyticsUtil.ACTION_LIKE : AnalyticsUtil.ACTION_UNLIKE,
                AnalyticsUtil.LABEL_ID, mPlace.getId());
        mFavouritePresenter.initialize(data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (containsFavouriteOption()) {
            menu.findItem(R.id.action_favourite).setIcon((mPlace.isFavourite() != null && mPlace
                    .isFavourite()) ? R.drawable
                    .icon_favourite : R.drawable.icon_not_favourite);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPlace = (Post) bundle.getSerializable(MyConstants.Extras.KEY_PLACE);
        }
        if (mPlace == null) throw new IllegalStateException("Place must be provided.");
        ((PlaceDetailDataBinding) mBinding).setPlace(mPlace);
        mOldFavouriteState = mPlace.isFavourite() != null ? mPlace.isFavourite() : false;

        initialize();

        UseCaseData data = new UseCaseData();
        List<Long> excludeList = new ArrayList<>();
        excludeList.add(mPlace.getId());
        data.putSerializable(UseCaseData.EXCLUDE_LIST, (Serializable) excludeList);
        mPresenter.initialize(data);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .dataModule(new DataModule(mPlace.getId(), null, MyConstants.DataParent.COUNTRY,
                        "place"))
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mFavouritePresenter.attachView(this);
        mPostShareCountPresenter.attachView(this);
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
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, mPlace.isFavourite());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, mPlace.getLikes());
        data.putExtra(MyConstants.Extras.KEY_SHARE_COUNT, mPlace.getUnSyncedShareCount());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void renderPlaces(List<Post> pPlaces) {
        ((PlaceDetailDataBinding) mBinding).setSimilarPlaces(pPlaces);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        boolean newFavouriteState = status ? !mOldFavouriteState : mOldFavouriteState;
        mPlace.setIsFavourite(newFavouriteState);
        int likes = mPlace.getLikes() == null ? 0 : mPlace.getLikes();
        mPlace.setLikes(newFavouriteState == mOldFavouriteState
                ? likes
                : newFavouriteState ? likes + 1 : likes - 1);
        ((PlaceDetailDataBinding) mBinding).setPlace(mPlace);
        mOldFavouriteState = mPlace.isFavourite();
        invalidateOptionsMenu();
    }

    @Override
    public void onViewCountUpdated() {
        mPlace.setUnSyncedViewCount(mPlace.getUnSyncedViewCount() + 1);
    }

    @Override
    public void onShareCountUpdate() {
        mPlace.setUnSyncedShareCount(mPlace.getUnSyncedShareCount()+1);
        ((PlaceDetailDataBinding) mBinding).setPlace(mPlace);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mPlacesContainer, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
