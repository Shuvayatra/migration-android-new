package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SyncResponseEntity {
    String status;
    @SerializedName("failed_ids")
    List<Long> failedIdList;
    @SerializedName("success_ids")
    List<Long> successIdList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String pStatus) {
        status = pStatus;
    }

    public List<Long> getFailedIdList() {
        return failedIdList;
    }

    public void setFailedIdList(List<Long> pFailedIdList) {
        failedIdList = pFailedIdList;
    }

    public List<Long> getSuccessIdList() {
        return successIdList;
    }

    public void setSuccessIdList(List<Long> pSuccessIdList) {
        successIdList = pSuccessIdList;
    }
}
