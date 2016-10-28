package com.taf.shuvayatra.ui.views;

import com.taf.model.Post;

public interface PostDetailView extends LoadDataView {
    void renderPost(Post post);

    void onPostFavouriteStateUpdated(Boolean status);

    void onViewCountUpdated();

    void onShareCountUpdate(boolean status);
}
