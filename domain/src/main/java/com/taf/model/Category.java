package com.taf.model;

import com.taf.util.MyConstants;

public class Category extends BaseModel {
    String title;
    String coverImageUrl;
    String iconUrl;
    String alias;
    String smallIconUrl;
    Long parentId;
    Long position;

    Long updatedAt;
    Long createdAt;

    String parentAlias;

    @Override
    public int getDataType() {
        if (getParentAlias().equals(MyConstants.SECTION.JOURNEY)) {
            return MyConstants.Adapter.TYPE_JOURNEY_CATEGORY;
        } else if (getParentAlias().equals(MyConstants.SECTION.COUNTRY)) {
            return MyConstants.Adapter.TYPE_COUNTRY;
        } else {
            return MyConstants.Adapter.TYPE_INFO;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pName) {
        title = pName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String pIconUrl) {
        iconUrl = pIconUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String pDetailImageUrl) {
        coverImageUrl = pDetailImageUrl;
    }

    public String getSmallIconUrl() {
        return smallIconUrl;
    }

    public void setSmallIconUrl(String pDetailIconUrl) {
        smallIconUrl = pDetailIconUrl;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long pParentId) {
        parentId = pParentId;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long pPosition) {
        position = pPosition;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String pAlias) {
        alias = pAlias;
    }

    public String getParentAlias() {
        return parentAlias;
    }

    public void setParentAlias(String pParentAlias) {
        parentAlias = pParentAlias;
    }

    @Override
    public String toString() {
        return title;
    }
}
