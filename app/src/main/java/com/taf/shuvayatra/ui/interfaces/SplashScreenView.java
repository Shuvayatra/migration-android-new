package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Post;

import java.util.List;

public interface SplashScreenView extends LatestContentView {
    void latestContentFetched(boolean hasNewContent);

    void unSyncedFavouritesSearched(List<Post> pUnSyncedPosts);

    void favouritesSynced(Boolean status);
}