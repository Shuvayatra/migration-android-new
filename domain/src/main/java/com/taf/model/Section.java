package com.taf.model;

import com.taf.util.MyConstants;

public class Section extends BaseModel {
    String alias;
    String title;
    Long updatedAt;
    Long createdAt;

    @Override
    public int getDataType() {
        return MyConstants.Adapter.TYPE_SECTION;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String pAlias) {
        alias = pAlias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long pUpdatedAt) {
        updatedAt = pUpdatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long pCreatedAt) {
        createdAt = pCreatedAt;
    }
}
