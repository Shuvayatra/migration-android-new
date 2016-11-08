package com.taf.data.repository;

import com.taf.data.BuildConfig;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Block;
import com.taf.model.Channel;
import com.taf.repository.IChannelRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by ngima on 11/3/16.
 */

public class ChannelRepository implements IChannelRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    private static final String TAG = "ChannelRepository";

    public ChannelRepository(DataStoreFactory mDataStoreFactory, DataMapper mDataMapper) {
        this.mDataStoreFactory = mDataStoreFactory;
        this.mDataMapper = mDataMapper;
    }

    @Override
    public Observable<List<Channel>> getChannelList() {

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getChannelList()
                .map(channelEntities -> mDataMapper.transformChannelList(channelEntities));

        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getChannelList()
                .map(channelEntities -> mDataMapper.transformChannelList(channelEntities));

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }

    @Override
    public Observable<List<Channel>> getCachedChannelList() {
        return mDataStoreFactory.createCacheDataStore()
                .getChannelList()
                .map(channelEntities -> mDataMapper.transformChannelList(channelEntities));

    }

}
