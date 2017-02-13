package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Notice;
import com.taf.model.Post;
import com.taf.model.ScreenDataModel;
import com.taf.model.ScreenModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseDynamicNavigationFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.ScreenDataPresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.BlockItemAnalytics;
import com.taf.shuvayatra.ui.interfaces.ListItemClickWithDataTypeListener;
import com.taf.shuvayatra.ui.views.ScreenDataView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;
import com.taf.util.MyConstants.DynamicScreen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by umesh on 1/13/17.
 */

public class BlockScreenFragment extends BaseDynamicNavigationFragment implements ScreenDataView,
        ListItemClickWithDataTypeListener, BlockItemAnalytics {

    public static final String TAG = "BlockScreenFragment";
    public static final String STATE_SCREEN = "screen";
    public static final String STATE_BLOCKS = "blocks";

    private ScreenModel mScreen;
    private BlocksAdapter mAdapter;

    @Inject
    ScreenDataPresenter presenter;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    ScreenDataModel<Block> mScreenModel;

    public static BlockScreenFragment newInstance(ScreenModel screen) {
        BlockScreenFragment fragment = new BlockScreenFragment();
        fragment.mScreen = screen;
        return fragment;
    }

    @Override
    public String screenName() {
        return "Navigation - " + mScreen.getTitle();
    }

    @Override
    public int getLayout() {
        return R.layout.item_empty_recycler_view;
    }

    @Override
    public Fragment defaultInstance(ScreenModel screenModel) {
        return newInstance(screenModel);
    }

    @Override
    public String fragmentTag() {
        return TAG + "_" + mScreen.getId();
    }

    @Override
    public String getScreenType() {
        return DynamicScreen.TYPE_BLOCK;
    }

    @Override
    public RecyclerView fragmentRecycler() {
        return mRecyclerView;
    }

    @Override
    public RecyclerView.ItemDecoration initDecorator() {
        return Utils.getBottomMarginDecoration(getContext(), R.dimen.mini_media_player_peek_height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpAdapter();

        if (savedInstanceState != null) {
            mScreen = (ScreenModel) savedInstanceState.get(STATE_SCREEN);
            initialize();
            mAdapter.setBlocks((List<BaseModel>) savedInstanceState.get(STATE_BLOCKS));
        } else {
            initialize();
            loadData();
        }
    }

    private void setUpAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new BlocksAdapter(getContext(), this, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initialize() {
        DaggerDataComponent.builder().applicationComponent(getTypedActivity().getApplicationComponent())
                .activityModule(getTypedActivity().getActivityModule())
                .dataModule(new DataModule(mScreen.getId()))
                .build()
                .inject(this);

        presenter.attachView(this);
    }

    private void loadData() {
        UseCaseData useCaseData = getUserCredentialsUseCase();
        useCaseData.putString(UseCaseData.END_POINT, mScreen.getEndPoint());
        useCaseData.putString(UseCaseData.SCREEN_DATA_TYPE, mScreen.getType());
        presenter.initialize(useCaseData);
    }

    @Override
    public void onListItemSelected(BaseModel model, int dataType, int adapterPosition) {

        if (dataType == MyConstants.Adapter.VIEW_TYPE_RADIO_WIDGET) {
            // send to radio widget
            getContext().sendBroadcast(new Intent(MyConstants.Intent.ACTION_SHOW_RADIO));
        }

        if (dataType == MyConstants.Adapter.VIEW_TYPE_NOTICE) {
            // dismiss notice
            mAdapter.getBlocks().remove(adapterPosition);
            mAdapter.notifyItemRemoved(adapterPosition);
            getTypedActivity().getPreferences().setNoticeDismissId(((Block) model).getNotice().getId());
        }

        if (dataType == MyConstants.Adapter.TYPE_COUNTRY_WIDGET) {
            // send to destination detail
            Intent intent = new Intent(MyConstants.Intent.ACTION_SHOW_DESTINATION);
            intent.putExtra(MyConstants.Extras.KEY_COUNTRY_WIDGET, model);
            getContext().sendBroadcast(intent);
        }
    }

    @Override
    public void onBlockItemSelected(Block block, Post post) {
        AnalyticsUtil.logBlockEvent(getAnalytics(), block.getTitle(), post.getTitle(),
                screenName(), block.getLayout());
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
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
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
    public void renderScreenData(ScreenDataModel model) {
        if (model != null) {
            this.mScreenModel = model;
            if (model.getData() != null && !model.getData().isEmpty()) {
                List<BaseModel> dataList = new ArrayList<>();

                dataList.addAll(model.getData());
                if (model.getNotice() != null)
                    dataList.add(Notice.convertToBlock(model.getNotice()));

                mAdapter.setBlocks(Utils.sortBlock(dataList));
            }
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_BLOCKS, (Serializable) mAdapter.getBlocks());
        outState.putSerializable(STATE_SCREEN, mScreen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }
}
