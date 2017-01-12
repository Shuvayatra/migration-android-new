package com.taf.data.repository.datasource;


import com.taf.data.cache.CacheImpl;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.ChannelEntity;
import com.taf.data.entity.CountryEntity;
import com.taf.data.entity.PodcastEntity;
import com.taf.data.entity.PostEntity;
import com.taf.model.Post;
import com.taf.data.entity.PostResponseEntity;
import com.taf.model.PostResponse;

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

    public Observable<List<CountryEntity>> getCountryList() {
        return mCache.getCountryList();
    }

    public Observable<PostEntity> getPostById(long id) {
        return mCache.getPost(id);
    }

    public Observable<List<PodcastEntity>> getPodcasts(Long channelId) {
        return mCache.getPodcastsByChannelId(channelId);
    }

    public Observable<List<PostEntity>> getPosts(int feedType, String params) {
        return mCache.getPostsByParams(feedType, params);
    }

    public Observable<PostEntity> getPost(Long id) {
        return mCache.getPost(id);
    }

    public Observable<List<BlockEntity>> getDestinationBlocks(long id) {
        return mCache.getDestinationBlocks(id);
    }

    public Observable<List<ChannelEntity>> getChannelList() {
        return mCache.getChannelList();
    }

    public Observable<List<Post>> getFavouritePosts() {
        return Observable.just(mCache.getFavourites());
    }

    public void saveFavouritePost(Post post) {
        mCache.saveFavourite(post);
    }

    public void removefavouritePost(Post post) {
        mCache.removeFavourite(post);
    }

    public Observable<PostResponseEntity> getNewsList() {
        return mCache.getNewsPosts();
    }

}
