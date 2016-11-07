package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.taf.model.BaseModel;
import com.taf.model.Channel;
import com.taf.model.HeaderItem;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.ChannelListPresenter;
import com.taf.shuvayatra.ui.adapter.ChannelAdapter;
import com.taf.shuvayatra.ui.views.ChannelView;
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

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    ChannelListPresenter mPresenter;

    ChannelAdapter mAdapter;

    public static ChannelFragment getInstance() {
        return new ChannelFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_channel;
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
                if(mAdapter.getItemViewType(position) == MyConstants.Adapter.TYPE_CHANNEL){
                    return 1;
                }
                return 2;
            }
        });

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void renderChannel(List<Channel> channelList) {
        Log.e(TAG, "renderChannel: channelList.size()>>" + channelList.size());


//        final long selctedChannelId = 4;

        Collections.sort(channelList, new Comparator<Channel>() {
            @Override
            public int compare(Channel o1, Channel o2) {
//                if(o1.getId() == selctedChannelId){
//                    return -1;
//                }
//                if(o2.getId() == selctedChannelId){
//                    return 1;
//                }
                return o1.getTitle().trim().toUpperCase().compareTo(o2.getTitle().trim().toUpperCase());
            }
        });

        List<BaseModel> allChannelList = new ArrayList<>();
        allChannelList.addAll(channelList);


//        for (Channel channel : channelList) {
//            if(channel.getId() == selctedChannelItem){
//                channel.setDataType(MyConstants.Adapter.TYPE_CHANNEL_SELECTED);
//                allChannelList.remove(channelList.indexOf(channel));
//                allChannelList.add(0, channel);
//
//                HeaderItem headerItem = new HeaderItem("Other Channels");
//                headerItem.setDataType(MyConstants.Adapter.TYPE_CHANNEL_HEADER);
//                allChannelList.add(1, headerItem);
//                break;
//            }
//        }


//        Channel selectedChannel = channelList.get(0);
//        selectedChannel.setDataType(MyConstants.Adapter
//                .TYPE_CHANNEL_SELECTED);
//        allChannelList.remove(0);
//        allChannelList.add(0, selectedChannel);
//
        HeaderItem headerItem = new HeaderItem("Channels");
        headerItem.setDataType(MyConstants.Adapter.TYPE_CHANNEL_HEADER);
        allChannelList.add(0, headerItem);

        mAdapter.setmChannels(allChannelList);
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
}
