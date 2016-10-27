package com.taf.shuvayatra.ui.views;

import com.taf.model.PostResponse;

public interface PostListView extends LoadDataView {
    void renderPostList(PostResponse response);
}
