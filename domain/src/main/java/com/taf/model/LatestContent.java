package com.taf.model;

import java.util.List;

/**
 * Created by julian on 12/7/15.
 */
public class LatestContent extends BaseModel {
    List<Post> mPosts;

    public List<Post> getPosts() {
        return mPosts;
    }

    public void setPosts(List<Post> pPosts) {
        mPosts = pPosts;
    }
}
