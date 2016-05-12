package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

public class SyncDataEntity {
    Long id;
    Boolean like;
    @SerializedName("view_count")
    Integer views;
    @SerializedName("share_count")
    Integer share;

    public SyncDataEntity(Long pId, Boolean plike, Integer viewCount, Integer pShare) {
        id = pId;
        like = plike;
        views = viewCount;
        share = pShare;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public Boolean getlike() {
        return like;
    }

    public void setlike(Boolean plike) {
        like = plike;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer pViews) {
        views = pViews;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer pShare) {
        share = pShare;
    }
}
