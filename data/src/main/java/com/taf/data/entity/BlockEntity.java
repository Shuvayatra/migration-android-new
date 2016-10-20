package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlockEntity {
    private int order;
    private String layout;
    private String title;
    private String description;
    @SerializedName("content")
    private List<PostEntity> data;
    private NoticeEntity notice;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PostEntity> getData() {
        return data;
    }

    public void setData(List<PostEntity> data) {
        this.data = data;
    }

    public NoticeEntity getNotice() {
        return notice;
    }

    public void setNotice(NoticeEntity notice) {
        this.notice = notice;
    }
}
