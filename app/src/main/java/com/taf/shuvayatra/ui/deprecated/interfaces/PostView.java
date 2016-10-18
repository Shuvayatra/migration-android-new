package com.taf.shuvayatra.ui.deprecated.interfaces;

import com.taf.model.Post;

/**
 * Created by Nirazan-PC on 5/6/2016.
 */
public interface PostView extends LoadDataView {
    public void postLoadCompleted(Post pPost);
}
