package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Post;

import java.util.List;

public interface SplashScreenView extends LoadDataView {

    void unSyncedFavouritesSearched(List<Post> pUnSyncedPosts);

    void favouritesSynced(Boolean status);
}