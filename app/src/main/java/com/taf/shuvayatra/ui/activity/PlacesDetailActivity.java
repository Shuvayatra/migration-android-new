package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.PlaceDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PlacesListPresenter;
import com.taf.shuvayatra.ui.interfaces.PlacesListView;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class PlacesDetailActivity extends BaseActivity implements PlacesListView {

    @Inject
    PlacesListPresenter mPresenter;

    @Bind(R.id.places_container)
    LinearLayout mPlacesContainer;
    Post mPlace;

    @Override
    public int getLayout() {
        return R.layout.activity_places_detail;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mPlace = (Post )bundle.getSerializable(MyConstants.Extras.KEY_PLACE);
        }
        if(mPlace == null) throw new IllegalStateException("Place must be provided.");
        ((PlaceDetailDataBinding) mBinding).setPlace(mPlace);

        initialize();
        mPresenter.initialize(null);
    }

    private void initialize(){
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .dataModule(new DataModule(1L, MyConstants.DataParent.COUNTRY, "place"))
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
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
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mPlacesContainer, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
