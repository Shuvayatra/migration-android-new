package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by julian on 10/26/16.
 */

public class PodcastResponseEntity {
    Long id;
    String title;
    @SerializedName("feeds")
    PaginatedEntity<PodcastEntity> data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PaginatedEntity<PodcastEntity> getData() {
        return data;
    }

    public void setData(PaginatedEntity<PodcastEntity> data) {
        this.data = data;
    }
}
