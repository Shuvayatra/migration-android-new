package com.taf.model;


import com.taf.util.MyConstants;

public class Notification extends BaseModel {
    String mTitle;
    String mDescription;
    Long mCreatedAt;
    Long mUpdatedAt;

    public Long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Long pCreatedAt) {
        mCreatedAt = pCreatedAt;
    }

    public Long getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Long pUpdatedAt) {
        mUpdatedAt = pUpdatedAt;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String pDescription) {
        mDescription = pDescription;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String pTitle) {
        mTitle = pTitle;
    }

    @Override
    public int getDataType() {
        return MyConstants.Adapter.TYPE_NOTIFICATION;
    }
}
