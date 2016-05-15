package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeletedContentDataEntity {

    List<DeletedContentEntity> posts;
    // TODO: 5/15/16 replace the serialized name as per api
    @SerializedName("my_sections")
    List<DeletedContentEntity> sections;

    public List<DeletedContentEntity> getPosts() {
        return posts;
    }

    public List<DeletedContentEntity> getSections() {
        return sections;
    }
}
