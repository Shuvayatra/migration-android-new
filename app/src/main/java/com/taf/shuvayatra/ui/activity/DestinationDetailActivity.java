package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.CountryInfo;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PlayerFragmentActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.presenter.DestinationBlocksPresenter;
import com.taf.shuvayatra.presenter.deprecated.CategoryPresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.shuvayatra.ui.views.DestinationDetailView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DestinationDetailActivity extends PlayerFragmentActivity implements
        DestinationDetailView,
        CountryView,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "DestinationDetailActivity";

    @Inject
    DestinationBlocksPresenter mPresenter;
    @Inject
    CountryListPresenter mCountryPresenter;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;

    BlocksAdapter mAdapter;
    Country mCountry;

    @Override
    public int getLayout() {
        return R.layout.activity_destination_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG, "oncreate called");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Logger.e(TAG, ": " + bundle.containsKey(MyConstants.Extras.KEY_COUNTRY));
            mCountry = (Country) bundle.get(MyConstants.Extras.KEY_COUNTRY);
            if (mCountry == null) {
                mCountry = new Country();
                mCountry.setId(bundle.getLong(MyConstants.Extras.KEY_COUNTRY_ID));
                mCountry.setTitle(bundle.getString(MyConstants.Extras.KEY_COUNTRY_TITLE));
                mCountry.setTitleEnglish(bundle.getString(MyConstants.Extras.KEY_COUNTRY_TITLE_EN));

                List<CountryInfo> infos = new ArrayList<>();
                mCountry.setInformation(infos);
            }
        }

        if (savedInstanceState != null) {
            AnalyticsUtil.logViewEvent(getAnalytics(), mCountry.getId(), mCountry.getTitle(),
                    "destination");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mCountry.getTitle());
        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .dataModule(new DataModule(mCountry.getId()))
                .build()
                .inject(this);

        mPresenter.attachView(this);
        mCountryPresenter.attachView(this);
        mAdapter = new BlocksAdapter(this);
        List<BaseModel> initList = new ArrayList<>();
        initList.add(mCountry);
        mAdapter.setBlocks(initList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mCountryPresenter.initialize(null);
        mPresenter.initialize(getUserCredentialsUseCase());
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void renderBlocks(List<Block> blocks) {
        List<BaseModel> models = new ArrayList<>();
        if (blocks.isEmpty()) {
            models.add(mCountry);
        } else {
            for (Block block : blocks) {
                if (block.getLayout().equalsIgnoreCase("notice")) {
                    if (block.getNotice() != null) {
                        models.add(0, block);
                    }
                    continue;
                }
                models.add(block);
            }
            if (!models.isEmpty()) {
                if (((Block) models.get(0)).getLayout().equalsIgnoreCase("notice")) {
                    models.add(1, mCountry);
                } else {
                    models.add(0, mCountry);
                }
            } else {
                models.add(mCountry);
            }
        }
        mAdapter.setBlocks(models);
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
        mPresenter.initialize(getUserCredentialsUseCase());
    }

    @Override
    public void renderCountries(List<Country> countryList) {
        for (Country country : countryList) {
            if (country.getId() == mCountry.getId()) {
                mCountry = country;
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
