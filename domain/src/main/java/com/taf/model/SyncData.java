package com.taf.model;

public class SyncData {
    public static final String STATUS_LIKE = "like";
    public static final String STATUS_DISLIKE = "dislike";

    Long id;
    Boolean like;
    Integer viewCount;
    Integer shareCount;

    public SyncData(Long pId, Boolean pLike, Integer pViewCount, Integer pShareCount) {
        id = pId;
        like = pLike;
        viewCount = pViewCount;
        shareCount = pShareCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean pLike) {
        like = pLike;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer pViewCount) {
        viewCount = pViewCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer pShareCount) {
        shareCount = pShareCount;
    }
}
