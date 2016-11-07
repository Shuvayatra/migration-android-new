package com.taf.data.entity;

/**
 * Created by ngima on 11/3/16.
 */

public class ChannelEntity {
    Long id;
    String title;
    String description;

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
}
