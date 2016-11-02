package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by julian on 10/18/16.
 */

public class NoticeEntity {
    Long id;
    String title;
    String description;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
