package com.taf.shuvayatra.ui.views;

import com.taf.model.PostResponse;

public interface SearchPostListView extends LoadDataView {
    void renderPosts(PostResponse postResponse);
}
