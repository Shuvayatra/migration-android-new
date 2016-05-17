package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeletedContentDataEntity {

    List<DeletedContentEntity> posts;
    @SerializedName("categories")
    List<DeletedContentEntity> sections;

    public List<DeletedContentEntity> getPosts() {
        return posts;
    }

    public List<DeletedContentEntity> getSections() {
        return sections;
    }
}
