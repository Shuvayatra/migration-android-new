package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.taf.model.BaseModel;
import com.taf.model.Country;
import com.taf.model.HeaderItem;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseNavigationFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.ui.adapter.CountryAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.taf.util.MyConstants.Adapter.TYPE_COUNTRY_HEADER;
import static com.taf.util.MyConstants.Adapter.TYPE_COUNTRY_SELECTED;
import static com.taf.util.MyConstants.Preferences.DEFAULT_LOCATION;

public class DestinationFragment extends BaseNavigationFragment
        implements CountryView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "DestinationFragment";

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;

    @Inject
    CountryListPresenter mPresenter;

    private CountryAdapter mAdapter;

    public static DestinationFragment newInstance() {
        return new DestinationFragment();
    }

    @Override
    public String screenName() {
        return "Navigation - Destination";
    }

    @Override
    public Fragment defaultInstance() {
        return newInstance();
    }

    @Override
    public String fragmentTag() {
        return TAG;
    }

    @Override
    public RecyclerView fragmentRecycler() {
        return mRecyclerView;
    }

    @Override
    public RecyclerView.ItemDecoration initDecorator() {
        return Utils.getBottomMarginDecorationForGrid(getContext(),
                R.dimen.mini_media_player_peek_height);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_destination;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("Destination");
        mSwipeRefreshLayout.setOnRefreshListener(this);
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .dataModule(new DataModule())
                .build()
                .inject(this);

        mPresenter.attachView(this);
        mPresenter.initialize(null);

        mAdapter = new CountryAdapter(getContext());
        mAdapter.setHasStableIds(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mRecyclerView.getAdapter().getItemViewType(position);
                if (type == MyConstants.Adapter.TYPE_COUNTRY) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        // WIP set animation time to zero for flickering but didnot work
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        mRecyclerView.getItemAnimator().setAddDuration(0);
        mRecyclerView.getItemAnimator().setRemoveDuration(0);
        mRecyclerView.getItemAnimator().setMoveDuration(0);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(getView(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void renderCountries(List<Country> countryList) {

        // copy list of country to list of base mScreenModel after sorting
        List<BaseModel> allList = new ArrayList<>();
        allList.addAll(countryList);

        HeaderItem headerItem = new HeaderItem(getString(R.string.all_country));
        headerItem.setDataType(TYPE_COUNTRY_HEADER);
        headerItem.setId(0l);
        allList.add(headerItem);

        String selectedCountry = ((BaseActivity) getActivity()).getPreferences().getLocation();

        if (!selectedCountry.equalsIgnoreCase(getString(R.string.country_not_decided_yet)) &&
                !selectedCountry.equalsIgnoreCase(DEFAULT_LOCATION)) {

            Country preferredCountry = Country.makeCountryFromPreference(selectedCountry);
            int index = allList.indexOf(preferredCountry);
            if (index != -1) {
                Country country = (Country) allList.get(index);
                country.setDataType(TYPE_COUNTRY_SELECTED);
            }
        }

        mAdapter.setCountries(Utils.sortCountry(allList));
    }

    @Override
    public void showLoadingView() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
    }

}
