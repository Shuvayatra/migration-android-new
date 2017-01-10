package com.taf.shuvayatra.ui.fragment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    RecyclerView mRecyclerView;
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
        final int margin = getResources().getDimensionPixelOffset(R.dimen.spacing_xxsmall);
//        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                super.onDraw(c, parent, state);
//            }
//
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                // TODO: 10/27/16 set margin properly
//                int i = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
//                int type = mAdapter.getItemViewType(i);
//                if (type == MyConstants.Adapter.TYPE_COUNTRY) {
//
////                    if (i % 2 == 0) {
////                        outRect.left = margin;
////                    } else {
////                        outRect.right = margin;
////                    }
//                }
//
//            }
//        });
        mRecyclerView.setAdapter(mAdapter);
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
                // TODO: 10/26/16 check if the english title is null or not. if not remove
//                if(o2.getTitleEnglish() == null){
//                    return -1;
//                } else if (o1.getTitleEnglish() == null){
//                    return 1;
//                }
                return o1.getTitleEnglish().trim().toUpperCase().compareTo(o2.getTitleEnglish().trim().toUpperCase());
            }
        });

        // copy list of country to list of base model after sorting
        List<BaseModel> allList = new ArrayList<>();
        allList.addAll(countryList);
        Logger.e(TAG, "all.size(): " + allList.size());
        String selectedCountry = ((BaseActivity) getActivity()).getPreferences().getLocation();
        Logger.e(TAG, "selectedCountry.substring(0,1): " + selectedCountry.substring(0, 1));

        if (selectedCountry.equals(MyConstants.Preferences.DEFAULT_LOCATION)) {
            HeaderItem headerItem = new HeaderItem(getString(R.string.all_country));
            headerItem.setDataType(MyConstants.Adapter.TYPE_COUNTRY_HEADER);

            allList.add(0, headerItem);
        } else {
            long id = Long.parseLong(selectedCountry.substring(0, selectedCountry.indexOf(",")));
            Logger.e(TAG, " selected id: " + id);
            for (Country country : countryList) {
                Logger.e(TAG, "country: " + country);
                if (country.getId() == id) {
                    country.setDataType(MyConstants.Adapter.TYPE_COUNTRY_SELECTED);
                    allList.remove(countryList.indexOf(country));
                    allList.add(0, country);
                    HeaderItem headerItem = new HeaderItem(getString(R.string.all_country));
                    headerItem.setDataType(MyConstants.Adapter.TYPE_COUNTRY_HEADER);
                    allList.add(1, headerItem);
                    break;
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
