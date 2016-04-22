package com.taf.model;

import java.util.List;

public class SimilarPost {
    List<Post> mPosts;

    public SimilarPost(List<Post> pPosts) {
        mPosts = pPosts;
    }

    public List<Post> getPosts() {
        return mPosts;
    }

    public void setPosts(List<Post> pPosts) {
        mPosts = pPosts;
    }
}
