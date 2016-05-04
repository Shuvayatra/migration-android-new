package com.taf.data.entity;

public class SyncDataEntity {
    Long id;
    String status;
    Integer views;

    public SyncDataEntity(Long pId, String pStatus, Integer viewCount) {
        id = pId;
        status = pStatus;
        views = viewCount;
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

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer pViews) {
        views = pViews;
    }
}
