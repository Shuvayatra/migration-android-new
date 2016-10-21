package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by julian on 10/24/16.
 */

public class PodcastEntity {
    Long id;
    String title;
    String description;
    String source;
    @SerializedName("image_url")
    String image;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
