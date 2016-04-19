package com.taf.data.entity;

import java.util.List;

/**
 * Created by julian on 12/7/15.
 */
public class LatestContentEntity {
    List<PostEntity> posts;

    public List<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(List<PostEntity> pPosts) {
        posts = pPosts;
    }
}
