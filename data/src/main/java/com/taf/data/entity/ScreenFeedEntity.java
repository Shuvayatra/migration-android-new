package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by umesh on 1/14/17.
 */

public class ScreenFeedEntity {
    private String title;
    private String name;
    @SerializedName("icon_image")
    private String icon;
    private String type;
    private PostResponseEntity feeds;

    public PostResponseEntity getFeeds() {
        return feeds;
    }

    public void setFeeds(PostResponseEntity feeds) {
        this.feeds = feeds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
