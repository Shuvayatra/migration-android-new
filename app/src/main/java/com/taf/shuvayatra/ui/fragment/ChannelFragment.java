package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Channel;
import com.taf.model.HeaderItem;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.base.PlayerFragmentActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.ChannelListPresenter;
import com.taf.shuvayatra.ui.activity.HomeActivity;
import com.taf.shuvayatra.ui.adapter.ChannelAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.views.ChannelView;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ngima on 11/3/16.
 */

public class ChannelFragment extends BaseFragment implements ChannelView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "ChannelFragment";

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;

    @Inject
    ChannelListPresenter mPresenter;

    ChannelAdapter mAdapter;

    public static ChannelFragment getInstance() {
        Logger.e(TAG, "instance created");
        return new ChannelFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.item_empty_recycler_view;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("Radio");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initialize();
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

        mAdapter = new ChannelAdapter(getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == MyConstants.Adapter.TYPE_CHANNEL) {
                    return 1;
                }
                return 2;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
    }


    @Override
    public void renderChannel(List<Channel> channelList) {
        Collections.sort(channelList, new Comparator<Channel>() {
            @Override
            public int compare(Channel o1, Channel o2) {
                return o1.getTitle().trim().toUpperCase().compareTo(o2.getTitle().trim().toUpperCase());
            }
        });

        List<BaseModel> allChannelList = new ArrayList<>();
        allChannelList.addAll(channelList);
        HeaderItem headerItem = new HeaderItem(getString(R.string.channels));
        headerItem.setDataType(MyConstants.Adapter.TYPE_CHANNEL_HEADER);
        if (allChannelList.size() > 0) {
            allChannelList.add(0, headerItem);
        }

        mAdapter.setChannels(allChannelList);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(getView(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
