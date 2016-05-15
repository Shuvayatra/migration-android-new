package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

public class DeletedContentEntity {

    Long id;
    @SerializedName("deleted_At")
    Long mDeletedAt;

    public Long getDeletedAt() {
        return mDeletedAt;
    }

    public void setDeletedAt(Long pDeletedAt) {
        mDeletedAt = pDeletedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }
}
