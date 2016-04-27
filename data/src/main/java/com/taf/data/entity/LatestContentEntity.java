package com.taf.data.entity;

import java.util.List;

public class LatestContentEntity {
    List<PostEntity> posts;
    List<SectionEntity> sections;

    public List<PostEntity> getPosts() {
        return posts;
    }

    public List<SectionEntity> getSections() {
        return sections;
    }
}
