package com.taf.data.repository.datasource;


import com.taf.data.cache.CacheImpl;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.PodcastEntity;
import com.taf.data.entity.PostEntity;

import java.util.List;

import rx.Observable;

public class CacheDataStore implements IDataStore {

    private CacheImpl mCache;

    public CacheDataStore(CacheImpl cache) {
        mCache = cache;
    }

    public Observable<List<BlockEntity>> getHomeBlocks() {
        return mCache.getHomeBlocks();
    }

    public Observable<List<BlockEntity>> getJourneyBlocks() {
        return mCache.getJourneyBlocks();
    }

    public Observable<PostEntity> getPostById(long id) {
        return mCache.getPost(id);
    }

    public Observable<List<PodcastEntity>> getPodcasts(Long channelId) {
        return mCache.getPodcastsByChannelId(channelId);
    }

    public Observable<List<PostEntity>> getPosts(String params) {
        return mCache.getPostsByParams(params);
    }

}
