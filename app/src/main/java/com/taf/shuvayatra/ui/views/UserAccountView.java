package com.taf.shuvayatra.ui.views;

import com.taf.model.Post;

import java.util.List;

/**
 * Created by umesh on 1/11/17.
 */

public interface UserAccountView extends LoadDataView {
    public void renderPosts(List<Post> posts);
}
