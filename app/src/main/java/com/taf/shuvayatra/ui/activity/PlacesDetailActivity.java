package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.PlaceDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PlacesListPresenter;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.ui.interfaces.PlacesListView;
import com.taf.shuvayatra.ui.interfaces.PostDetailView;
import com.taf.util.MyConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class PlacesDetailActivity extends BaseActivity implements PlacesListView, PostDetailView {

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
    public boolean containsFavouriteOption() {
        return true;
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
                .dataModule(new DataModule(1L, MyConstants.DataParent.COUNTRY, "place"))
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mFavouritePresenter.attachView(this);
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
        mPlace.setIsFavourite(status ? !mOldFavouriteState : mOldFavouriteState);
        invalidateOptionsMenu();
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
