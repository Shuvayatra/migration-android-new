package com.taf.data.cache;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taf.data.entity.BlockEntity;
import com.taf.data.entity.ChannelEntity;
import com.taf.data.entity.CountryEntity;
import com.taf.data.entity.PodcastEntity;
import com.taf.data.entity.PodcastResponseEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.PostResponseEntity;
import com.taf.data.entity.ScreenBlockEntity;
import com.taf.data.entity.ScreenEntity;
import com.taf.data.entity.ScreenFeedEntity;
import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.util.MyConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class CacheImpl {

    public static final String TAG = "CacheImpl";
    public static final String POST_PREFIX = "post-";
    public static final String POST_LIST_PREFIX = "post-param-";
    public static final String PODCAST_PREFIX = "podcast-";
    private static final String HOME_BLOCKS = "home-cache";
    private static final String NEWS_BLOCKS = "news-cache";
    private static final String JOURNEY_BLOCKS = "journey-blocks";
    private static final String POSTS = "posts";
    private static final String COUNTRY_LIST = "country-list";
    public static final String DESTINATION_BLOCKS_SUFFIX = "destination-blocks-";
    public static final String CHANNEL_LIST = "channel-list";
    public static final String FAVOURITE_POST = "favourite-post";
    public static final String SCREENS = "screens";
    public static final String SCREEN_DATA = "screen-data";

    private SimpleDiskCache mSimpleDiskCache;

    @Inject
    public CacheImpl(Context context) {
        try {
            mSimpleDiskCache = SimpleDiskCache.open(context.getFilesDir(), MyConstants.APP_CACHE_VERSION, MyConstants.MAX_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.e(TAG, "mSimpleDiskCache: " + mSimpleDiskCache);
    }

    //// TODO: 10/25/16 refactor methods as per the objects to cache

    public void saveHomeBlocks(List<BlockEntity> blockEntities) {
        saveBlocks(HOME_BLOCKS, blockEntities);
    }

    public Observable<List<BlockEntity>> getHomeBlocks() {
        return getBlocks(HOME_BLOCKS);
    }

    public void saveJourneyBlocks(List<BlockEntity> blockEntities) {
        saveBlocks(JOURNEY_BLOCKS, blockEntities);
    }

    public void saveDestinationBlocks(long id, List<BlockEntity> blockEntities) {
        saveBlocks(DESTINATION_BLOCKS_SUFFIX + id, blockEntities);
    }

    public Observable<List<BlockEntity>> getDestinationBlocks(long id) {
        return getBlocks(DESTINATION_BLOCKS_SUFFIX + id);
    }

    public Observable<List<BlockEntity>> getJourneyBlocks() {
        return getBlocks(JOURNEY_BLOCKS);
    }

    public Observable<List<CountryEntity>> getCountryList() {
        List<CountryEntity> countryEntities = new ArrayList<>();
        try {
            if (mSimpleDiskCache.contains(COUNTRY_LIST)) {
                String json = mSimpleDiskCache.getCachedString(COUNTRY_LIST).getValue();
                countryEntities = new Gson().fromJson(json,
                        new TypeToken<List<CountryEntity>>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Observable.just(countryEntities);
    }

    private void saveBlocks(String key, List<BlockEntity> blockEntities) {
        Logger.e(TAG, "========= saving into cache: ========");
        Logger.e(TAG, "blockEntities: " + blockEntities.size());
        try {
            mSimpleDiskCache.put(key, new Gson().toJson(blockEntities));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.e(TAG, "======= saved into cache =========");
    }

    public void saveCountryList(List<CountryEntity> countryList) {
        try {
            mSimpleDiskCache.put(COUNTRY_LIST, new Gson().toJson(countryList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Observable<List<BlockEntity>> getBlocks(String key) {
        Logger.e(TAG, "========= getting home cache =========");
        List<BlockEntity> blocks = new ArrayList<>();
        try {
            if (mSimpleDiskCache.contains(key)) {
                String json = mSimpleDiskCache.getCachedString(key).getValue();
                blocks = new Gson().fromJson(json,
                        new TypeToken<List<BlockEntity>>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.e(TAG, "blocks: " + blocks.size());
        Logger.e(TAG, " =============== finishes getting home cache ========");
        return Observable.just(blocks);
    }

    public void savePodcastsByChannelId(PodcastResponseEntity entities, long channelId) {
        PodcastResponseEntity podcastResponseEntity = getPodcastsByChannelId(channelId);
        if (podcastResponseEntity == null) {
            try {
                Logger.e(TAG, "podcast cache saved: " + entities.getData().getData().size());
                mSimpleDiskCache.put(PODCAST_PREFIX + channelId, new Gson().toJson(entities));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        podcastResponseEntity.getData().setCurrentPage(entities.getData().getCurrentPage());
        podcastResponseEntity.getData().setLastPage(entities.getData().getLastPage());
        podcastResponseEntity.getData().setTotal(entities.getData().getTotal());

        Logger.e(TAG, "podcast cache prevoius: " + podcastResponseEntity.getData().getData().size());
        podcastResponseEntity.getData().getData().addAll(entities.getData().getData());
        podcastResponseEntity.getData().setData(new ArrayList<>(new LinkedHashSet<PodcastEntity>(podcastResponseEntity.getData().getData())));
        Logger.e(TAG, "podcast cache new: " + podcastResponseEntity.getData().getData().size());

        try {
            Logger.e(TAG, "podcast cache saved: " + entities.getData().getData().size());
            mSimpleDiskCache.put(PODCAST_PREFIX + channelId, new Gson().toJson(podcastResponseEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public PodcastResponseEntity getPodcastsByChannelId(Long channelId) {
        PodcastResponseEntity podcasts = null;
        String key = PODCAST_PREFIX + channelId;
        try {
            if (mSimpleDiskCache.contains(key)) {
                String json = mSimpleDiskCache.getCachedString(key).getValue();
                podcasts = new Gson().fromJson(json,
                        new TypeToken<PodcastResponseEntity>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return podcasts;
    }


    // TODO: 10/26/16 define key as per categories/blocks
    public void savePosts(int feedType, String params, List<PostEntity> entities, boolean append) {
        String suffix = "";
        if (params != null)
            suffix = params.replaceAll(",", "-");
        if (append) {
            Logger.d("CacheImpl_savePosts", "append");
            appendPosts(feedType == 0 ? POST_LIST_PREFIX : NEWS_BLOCKS + suffix, entities);
        } else {
            savePosts(feedType == 0 ? POST_LIST_PREFIX : NEWS_BLOCKS + suffix, entities);
        }
    }

    public void saveNews(List<PostEntity> entities, boolean append) {
        if (append) {
            appendNews(entities);
        } else {
            saveNews(entities);
        }
    }

    public void saveNews(List<PostEntity> entities) {
        try {
            mSimpleDiskCache.put(NEWS_BLOCKS, new Gson().toJson(entities));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendNews(List<PostEntity> entities) {
        List<PostEntity> list = getPosts(NEWS_BLOCKS);
        list.addAll(entities);
        savePosts(NEWS_BLOCKS, list);
    }

    public Observable<List<PostEntity>> getPostsByParams(int feedType, String params) {
        String suffix = "";
        if (params != null)
            suffix = params.replaceAll(",", "-");
        return Observable.just(getPosts(feedType == 0 ? POST_LIST_PREFIX : NEWS_BLOCKS + suffix));
    }

    public void savePosts(String key, List<PostEntity> posts) {
        try {
            mSimpleDiskCache.put(key, new Gson().toJson(posts));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendPosts(String key, List<PostEntity> posts) {
        List<PostEntity> list = getPosts(key);
        list.addAll(posts);
        savePosts(key, list);
    }

    public List<PostEntity> getPosts(String key) {
        List<PostEntity> posts = new ArrayList<>();
        try {
            if (mSimpleDiskCache.contains(key)) {
                String json = mSimpleDiskCache.getCachedString(key).getValue();
                posts = new Gson().fromJson(json,
                        new TypeToken<List<PostEntity>>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public Observable<PostEntity> getPost(long id) {

        PostEntity post = null;
        try {
            String key = POST_PREFIX + id;
            if (mSimpleDiskCache.contains(key)) {
                String json = mSimpleDiskCache.getCachedString(String.valueOf(key)).getValue();
                post = new Gson().fromJson(json,
                        new TypeToken<PostEntity>() {
                        }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Observable.just(post);
    }

    public void savePost(PostEntity post) {
        try {
            mSimpleDiskCache.put(POST_PREFIX + post.getId(), new Gson().toJson(post));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Observable<List<ChannelEntity>> getChannelList() {
        List<ChannelEntity> channelEntities = new ArrayList<>();
        try {
            if (mSimpleDiskCache.contains(CHANNEL_LIST)) {
                String json = mSimpleDiskCache.getCachedString(CHANNEL_LIST).getValue();
                channelEntities = new Gson().fromJson(json, new TypeToken<List<ChannelEntity>>() {

                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Observable.just(channelEntities);
    }


    public void saveChannelList(List<ChannelEntity> channelList) {
        try {
            mSimpleDiskCache.put(CHANNEL_LIST, new Gson().toJson(channelList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNewsBlocks(PostResponseEntity blockEntities) {
        try {
            mSimpleDiskCache.put(NEWS_BLOCKS, new Gson().toJson(blockEntities));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Observable<PostResponseEntity> getNewsPosts() {
        PostResponseEntity postResponseEntity = new PostResponseEntity();
        String jsonObjectString = "";
        try {
            Log.e(TAG, "getNewsPosts: " + mSimpleDiskCache.getCachedString(NEWS_BLOCKS).getValue());
            try {
                JSONArray array = new JSONArray(mSimpleDiskCache.getCachedString(NEWS_BLOCKS).getValue());
                jsonObjectString = array.get(0).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            postResponseEntity = new Gson().fromJson(jsonObjectString, new TypeToken<PostResponseEntity>() {

            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Observable.just(postResponseEntity);
    }

    public void saveFavourite(Post post) {

        List<Post> posts = getFavourites();
        posts.add(post);
        try {
            mSimpleDiskCache.put(FAVOURITE_POST, new Gson().toJson(posts));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFavourite(Post post) {
        List<Post> posts = getFavourites();
        if (posts.remove(post)) {
            try {
                mSimpleDiskCache.put(FAVOURITE_POST, new Gson().toJson(posts));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Post> getFavourites() {
        List<Post> posts = new ArrayList<>();
        try {
            if (mSimpleDiskCache.contains(FAVOURITE_POST)) {

                String json = mSimpleDiskCache.getCachedString(FAVOURITE_POST).getValue();
                posts = new Gson().fromJson(json, new TypeToken<List<Post>>() {
                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public void saveScreens(List<ScreenEntity> entity) {
        if (entity != null) {
            try {
                mSimpleDiskCache.put(SCREENS, new Gson().toJson(entity, new TypeToken<List<ScreenEntity>>() {
                }.getType()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ScreenEntity> getScreens() {
        List<ScreenEntity> entities = new ArrayList<>();
        if (mSimpleDiskCache.contains(SCREENS)) {
            try {
                String json = mSimpleDiskCache.getCachedString(SCREENS).getValue();
                entities = new Gson().fromJson(json, new TypeToken<List<ScreenEntity>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return entities;
    }

    public void saveScreenBlockData(long id,ScreenBlockEntity entity) {
        if (entity != null) {
            String key = SCREEN_DATA + id;
            try {
                mSimpleDiskCache.put(key, new Gson().toJson(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ScreenBlockEntity getScreenBlockData(long id) {

        ScreenBlockEntity screenBlockEntity = null;
        if (mSimpleDiskCache.contains(SCREEN_DATA + id)) {
            try {
                if (mSimpleDiskCache.contains(SCREEN_DATA + id)) {
                    String json = mSimpleDiskCache.getCachedString(SCREEN_DATA + id).getValue();
                    screenBlockEntity = new Gson().fromJson(json, new TypeToken<ScreenBlockEntity>() {
                    }.getType());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return screenBlockEntity;
    }

    public ScreenFeedEntity getScreenFeedData(long id) {

        ScreenFeedEntity screenFeedEntity = null;
        if (mSimpleDiskCache.contains(SCREEN_DATA + id)) {

            try {
                if (mSimpleDiskCache.contains(SCREEN_DATA + id)) {
                    String json = mSimpleDiskCache.getCachedString(SCREEN_DATA + id).getValue();
                    screenFeedEntity = new Gson().fromJson(json, new TypeToken<ScreenFeedEntity>() {
                    }.getType());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return screenFeedEntity;
    }

    public void saveScreenFeedData(long id, ScreenFeedEntity entity) {
        ScreenFeedEntity screenFeedEntity = getScreenFeedData(id);
        String key = SCREEN_DATA + id;
        // if nothing in cache just put data in cache
        if (screenFeedEntity == null) {
            try {
                mSimpleDiskCache.put(key, new Gson().toJson(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // update the cache
        screenFeedEntity.getFeeds().setTotal(entity.getFeeds().getTotal());
        screenFeedEntity.getFeeds().setCurrentPage(entity.getFeeds().getCurrentPage());
        screenFeedEntity.getFeeds().setLastPage(entity.getFeeds().getLastPage());
        screenFeedEntity.getFeeds().setLimit(entity.getFeeds().getLimit());

        screenFeedEntity.getFeeds().getData().addAll(entity.getFeeds().getData());
        screenFeedEntity.getFeeds().setData(new ArrayList<>(new LinkedHashSet<PostEntity>(screenFeedEntity.getFeeds().getData())));

        try {
            mSimpleDiskCache.put(key, new Gson().toJson(screenFeedEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
