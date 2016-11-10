package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.PaginatedData;
import com.taf.model.PodcastResponse;
import com.taf.repository.IPodcastRepository;

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
    public Observable<PodcastResponse> getPodcasts() {
        Observable apiObservable = mDataStoreFactory.createRestDataStore()
                .getPodcasts(mChannelId)
                .map(response -> mDataMapper.transformPodcastResponse(response));

        Observable cacheObservable = mDataStoreFactory.createCacheDataStore()
                .getPodcasts(mChannelId)
                .map(entities -> {
                    PodcastResponse response = new PodcastResponse(mChannelId);
                    PaginatedData paginatedData = new PaginatedData(1, 1);
                    paginatedData.setTotal(entities.size());
                    paginatedData.setData(mDataMapper.transformPodcastEntity(entities));
                    response.setData(paginatedData);
                    return response;
                });

        return Observable.concatDelayError(cacheObservable, apiObservable);
    }
}
