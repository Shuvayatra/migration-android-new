package com.taf.model;

public class SyncData {
    public static final String STATUS_LIKE = "like";
    public static final String STATUS_DISLIKE = "dislike";

    Long id;
    String status;
    Integer viewCount;

    public SyncData(Long pId, String pStatus,Integer pViewCount) {
        id = pId;
        status = pStatus;
        viewCount = pViewCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String pStatus) {
        status = pStatus;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer pViewCount) {
        viewCount = pViewCount;
    }
}
