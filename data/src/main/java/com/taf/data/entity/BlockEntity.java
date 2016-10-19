package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlockEntity {
    private String layout;
    private String title;
    @SerializedName("content")
    private List<PostEntity> data;

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PostEntity> getData() {
        return data;
    }

    public void setData(List<PostEntity> data) {
        this.data = data;
    }
}
