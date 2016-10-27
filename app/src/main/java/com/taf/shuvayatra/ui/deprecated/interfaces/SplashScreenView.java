package com.taf.shuvayatra.ui.deprecated.interfaces;

import com.taf.model.Post;
import com.taf.shuvayatra.ui.views.LoadDataView;

import java.util.List;

public interface SplashScreenView extends LoadDataView {

    void unSyncedFavouritesSearched(List<Post> pUnSyncedPosts);

    void favouritesSynced(Boolean status);
}