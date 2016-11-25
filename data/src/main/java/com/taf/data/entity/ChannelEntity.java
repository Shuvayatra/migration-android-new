package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ngima on 11/3/16.
 */

public class ChannelEntity {
    Long id;
    String title;
    String description;
    @SerializedName("image_url")
    String imageUrl;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
