package com.taf.data.entity;

public class SyncDataEntity {
    Long id;
    String status;

    public SyncDataEntity(Long pId, String pStatus) {
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
