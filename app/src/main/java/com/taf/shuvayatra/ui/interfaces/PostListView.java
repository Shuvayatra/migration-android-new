package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Post;

import java.util.List;

public interface PostListView extends LoadDataView {
    void renderPostList(List<Post> pPosts, int pOffset, int pTotalCount);
}
