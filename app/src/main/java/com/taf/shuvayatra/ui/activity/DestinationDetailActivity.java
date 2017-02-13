package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.CountryInfo;
import com.taf.model.Notice;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PlayerFragmentActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.presenter.DestinationBlocksPresenter;
import com.taf.shuvayatra.presenter.deprecated.CategoryPresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.BlockItemAnalytics;
import com.taf.shuvayatra.ui.interfaces.ListItemClickWithDataTypeListener;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.shuvayatra.ui.views.DestinationDetailView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;
import com.taf.util.MyConstants.Adapter;
import com.taf.util.MyConstants.Extras;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DestinationDetailActivity extends PlayerFragmentActivity implements
        DestinationDetailView,
        CountryView,
        SwipeRefreshLayout.OnRefreshListener,
        ListItemClickWithDataTypeListener,
        BlockItemAnalytics {

    public static final String TAG = "DestinationDetailActivity";
    public static final String SCREEN_NAME = "Destination Screen";

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
    @BindView(R.id.searchbox_container)
    LinearLayout mSearchBox;

    private BlocksAdapter mAdapter;
    private Country mCountry;
    private Long destinationId = Long.MIN_VALUE;

    @Override
    public String screenName() {
        return SCREEN_NAME;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_destination_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isFromDeeplink())
            mCountry = (Country) getIntent().getExtras().get(Extras.KEY_COUNTRY);
        else
            destinationId = Long.parseLong(getIntent().getData()
                    .getQueryParameter(MyConstants.Deeplink.PARAM_DESTINATION_ID));

        if (mCountry != null) {
            Logger.e(TAG, ">>> NAME: " + mCountry.getTitle());
            Logger.e(TAG, ">>> INFOs: " + mCountry.getAllInformation());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateViewAndAnalytics(mCountry);

        initialize();

        mSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    private void updateViewAndAnalytics(Country country) {
        if (mCountry != null) {
            AnalyticsUtil.logViewEvent(getAnalytics(), mCountry.getId(), mCountry.getTitle(),
                    "destination");
            getSupportActionBar().setTitle(mCountry.getTitle());
        }
    }

    public boolean isFromDeeplink() {
        return getIntent() != null && getIntent().getData() != null && getIntent().getData()
                .getQueryParameterNames().contains(MyConstants.Deeplink.PARAM_DESTINATION_ID);
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

    @Override
    public void onBlockItemSelected(Block block, Post post) {
        AnalyticsUtil.logBlockEvent(getAnalytics(), block.getTitle(), post.getTitle(),
                screenName(), block.getLayout());
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .dataModule(new DataModule(mCountry == null ? destinationId : mCountry.getId()))
                .build()
                .inject(this);

        mPresenter.attachView(this);
        mCountryPresenter.attachView(this);
        mAdapter = new BlocksAdapter(this, this, this);
        List<BaseModel> initList = new ArrayList<>();

        if (mCountry != null)
            initList.add(mCountry);

        mAdapter.setHasStableIds(true);
        mAdapter.setBlocks(initList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (isMediaPlayerVisible())
            mRecyclerView.addItemDecoration(Utils.getBottomMarginDecoration(getApplicationContext(),
                    R.dimen.mini_media_player_peek_height));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (isFromDeeplink()) {
            UseCaseData data = new UseCaseData();
            data.putBoolean(UseCaseData.CACHED_DATA, true);
            data.putBoolean(UseCaseData.SHOULD_SHOW_LOADING, true);
            mCountryPresenter.initialize(data);
        }

        mPresenter.initialize(getUserCredentialsUseCase());
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemSelected(BaseModel model, int dataType, int adapterPosition) {

        if (dataType == Adapter.VIEW_TYPE_NOTICE) {
            // dismiss notice
            mAdapter.getBlocks().remove(adapterPosition);
            mAdapter.notifyItemRemoved(adapterPosition);
            getPreferences().setNoticeDismissId(((Block) model).getNotice().getId());
        }
    }

    @Override
    public void onDeeplinkSelected(String deeplink, BaseModel model, int dataType, int adapterPosition) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
        if (dataType == MyConstants.Adapter.VIEW_TYPE_NOTICE) {
            Notice notice = ((Block) model).getNotice();
            intent.putExtra(MyConstants.Extras.KEY_PAGE_TITLE, notice.getTitle());
            intent.putExtra(MyConstants.Extras.KEY_ID, notice.getId());

            AnalyticsUtil.logReadMoreEvent(getAnalytics(), model.getId(), ((Block) model).getNotice()
                    .getTitle(), screenName(), ((Block) model).getLayout());
        } else {
            intent.putExtra(MyConstants.Extras.KEY_PAGE_TITLE, ((Block) model).getTitle());
            intent.putExtra(MyConstants.Extras.KEY_POST_ID, model.getId());

            AnalyticsUtil.logReadMoreEvent(getAnalytics(), model.getId(), ((Block) model).getTitle(),
                    screenName(), ((Block) model).getLayout());
        }
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void renderBlocks(List<Block> blocks) {
        List<BaseModel> models = new ArrayList<>();
        models.addAll(blocks);
        if (mCountry != null)
            models.add(mCountry);
        mAdapter.setBlocks(Utils.sortBlock(models));
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
        mPresenter.initialize(getUserCredentialsUseCase());
    }

    @Override
    public void renderCountries(List<Country> countryList) {
        // check and add country to list
        BaseModel model = new BaseModel();
        model.setId(destinationId);
        int index = countryList.indexOf(model);
        if (index != -1) {
            mCountry = countryList.get(index);
            Logger.e(TAG, ">>> NAME: " + mCountry.getTitle());
            Logger.e(TAG, ">>> INFOs: " + mCountry.getAllInformation());
            updateViewAndAnalytics(mCountry);
            if (!mAdapter.getBlocks().contains(mCountry)) {
                mAdapter.getBlocks().add(mCountry);
                Utils.sortBlock(mAdapter.getBlocks());
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
