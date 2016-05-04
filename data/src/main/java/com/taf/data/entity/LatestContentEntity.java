package com.taf.data.entity;

import java.util.List;

public class LatestContentEntity {
    List<PostEntity> posts;
    List<CategoryEntity> sections;

    public List<PostEntity> getPosts() {
        return posts;
    }

    public List<CategoryEntity> getCategories() {
        return sections;
    }
}
