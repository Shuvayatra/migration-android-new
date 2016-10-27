package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Podcast;
import com.taf.repository.IPodcastRepository;

import java.util.List;

import rx.Observable;

public class PodcastRepository implements IPodcastRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    private final Long mChannelId;

    public PodcastRepository(Long channelId, DataStoreFactory pDataStoreFactory, DataMapper
            pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;

        mChannelId = channelId;
    }

    @Override
    public Observable<List<Podcast>> getPodcasts() {
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getPodcasts(mChannelId)
                .map(podcastEntities -> mDataMapper.transformPodcastEntity(podcastEntities));

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getPodcasts(mChannelId)
                .map(podcastEntities -> mDataMapper.transformPodcastEntity(podcastEntities));

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }
}
