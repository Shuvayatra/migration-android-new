package com.taf.shuvayatra.ui.deprecated.interfaces;

import com.taf.model.Post;

import java.util.List;

public interface PostListView extends LoadDataView {
    void renderPostList(List<Post> pPosts, int pTotalCount);
}
