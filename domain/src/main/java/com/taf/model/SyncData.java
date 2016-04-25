package com.taf.model;

public class SyncData {
    public static final String STATUS_LIKE = "like";
    public static final String STATUS_DISLIKE = "dislike";

    Long id;
    String status;

    public SyncData(Long pId, String pStatus) {
        id = pId;
        status = pStatus;
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
}
