package com.taf.data.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.util.MyConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class CacheImpl {

    public static final String TAG = "CacheImpl";

    private SimpleDiskCache mSimpleDiskCache;
    private static final String HOME_BLOCKS = "home-cache";
    private static final String JOURNEY_BLOCKS = "journey-blocks";
    private static final String POSTS = "posts";
    public static final String POST_SUFFIX = "post-";

    @Inject
    public CacheImpl(Context context){
        try {
            mSimpleDiskCache = SimpleDiskCache.open(context.getFilesDir(), MyConstants.APP_CACHE_VERSION, MyConstants.MAX_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.e(TAG,"mSimpleDiskCache: "+ mSimpleDiskCache);
    }

    //// TODO: 10/25/16 refactor methods as per the objects to cache

    public void saveHomeBlocks(List<BlockEntity> blockEntities){
        saveBlocks(HOME_BLOCKS,blockEntities);
    }
    
    public Observable<List<BlockEntity>> getHomeBlocks(){
       return getBlocks(HOME_BLOCKS);
    }

    public void saveJourneyBlocks(List<BlockEntity> blockEntities){
        saveBlocks(JOURNEY_BLOCKS, blockEntities);
    }

    public Observable<List<BlockEntity>> getJourneyBlocks(){
        return getBlocks(JOURNEY_BLOCKS);
    }

    private void saveBlocks(String key, List<BlockEntity> blockEntities){
        Logger.e(TAG,"========= saving into cache: ========");
        Logger.e(TAG,"blockEntities: "+ blockEntities.size());
        try {
            mSimpleDiskCache.put(key, new Gson().toJson(blockEntities));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.e(TAG,"======= saved into cache =========");
    }

    private Observable<List<BlockEntity>> getBlocks(String key){
        Logger.e(TAG,"========= getting home cache =========");
        List<BlockEntity> blocks = new ArrayList<>();
        try {
            if(mSimpleDiskCache.contains(key)) {
                String json = mSimpleDiskCache.getCachedString(key).getValue();
                blocks = new Gson().fromJson(json,
                        new TypeToken<List<BlockEntity>>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.e(TAG,"blocks: "+ blocks.size());
        Logger.e(TAG," =============== finishes getting home cache ========");
        return Observable.just(blocks);
    }

    // TODO: 10/26/16 define key as per categories/blocks

    public void savePosts(String key, List<PostEntity> posts){
        try {
            mSimpleDiskCache.put(POSTS, new Gson().toJson(posts));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Observable<List<PostEntity>> getPosts(String key){
        List<PostEntity> posts = new ArrayList<>();
        try {
            if(mSimpleDiskCache.contains(key)) {
                String json = mSimpleDiskCache.getCachedString(key).getValue();
                posts = new Gson().fromJson(json,
                        new TypeToken<List<PostEntity>>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Observable.just(posts);
    }

    public Observable<PostEntity> getPost(long id){

        PostEntity post = null;
        try {
            if(mSimpleDiskCache.contains(POST_SUFFIX + id)) {
                String json = mSimpleDiskCache.getCachedString(String.valueOf(id)).getValue();
                post = new Gson().fromJson(json,
                        new TypeToken<PostEntity>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Observable.just(post);
    }

    public void savePost(PostEntity post){
        try {
            mSimpleDiskCache.put(POST_SUFFIX+post.getId(), new Gson().toJson(post));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendPosts(String key, List<PostEntity> posts){
        getPosts(key).doOnNext(postsEntities -> {
            postsEntities.addAll(posts);
            savePosts(POSTS, postsEntities);
        });
    }

}
