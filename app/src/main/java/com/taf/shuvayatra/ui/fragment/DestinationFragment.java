package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.widget.RelativeLayout;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Country;
import com.taf.model.HeaderItem;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.ui.adapter.CountryAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DestinationFragment extends BaseFragment implements CountryView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "DestinationFragment";

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;

    @Inject
    CountryListPresenter mPresenter;

    CountryAdapter mAdapter;


    public static DestinationFragment newInstance() {

        DestinationFragment fragment = new DestinationFragment();
        return fragment;
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mAdapter.getItemViewType(position);
                Logger.e(TAG, "span type: " + type);
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
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(getView(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void renderCountries(List<Country> countryList) {
        Logger.e(TAG, "countryList.size(): " + countryList.size());


        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if (o2.getTitleEnglish() == null && o1.getTitleEnglish() != null) {
                    return -1;
                } else if (o1.getTitleEnglish() == null && o2.getTitleEnglish() != null) {
                    return 1;
                } else if (o1.getTitleEnglish() == null && o2.getTitleEnglish() == null) {
                    return 0;
                }
                return o1.getTitleEnglish().trim().toUpperCase().compareTo(o2.getTitleEnglish().trim().toUpperCase());
            }
        });

        // copy list of country to list of base mScreenModel after sorting
        List<BaseModel> allList = new ArrayList<>();
        allList.addAll(countryList);
        Logger.e(TAG, "all.size(): " + allList.size());
        String selectedCountry = ((BaseActivity) getActivity()).getPreferences().getLocation();

        if (!selectedCountry.equalsIgnoreCase(getString(R.string.country_not_decided_yet))) {

            if (selectedCountry.equals(MyConstants.Preferences.DEFAULT_LOCATION)) {
                HeaderItem headerItem = new HeaderItem(getString(R.string.all_country));
                headerItem.setDataType(MyConstants.Adapter.TYPE_COUNTRY_HEADER);

                if (allList.size() > 0) {
                    allList.add(0, headerItem);
                }
            } else {
                long id = Long.parseLong(selectedCountry.substring(0, selectedCountry.indexOf(",")));
                Logger.e(TAG, " selected id: " + id);
                for (Country country : countryList) {
                    Logger.e(TAG, "country: " + country);
                    if (country.getId() == id) {
                        country.setDataType(MyConstants.Adapter.TYPE_COUNTRY_SELECTED);
                        allList.remove(countryList.indexOf(country));
                        if (allList.size() > 0) {
                            allList.add(0, country);
                        }
                        HeaderItem headerItem = new HeaderItem(getString(R.string.all_country));
                        headerItem.setDataType(MyConstants.Adapter.TYPE_COUNTRY_HEADER);
                        allList.add(1, headerItem);
                        break;
                    }
                }

            }
        }

        mAdapter.setCountries(allList);
    }

    @Override
    public void showLoadingView() {

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
