package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

public class CategoryEntity {
    Long id;
    @SerializedName("parent_id")
    Long parentId;
    Long position;
    String title;
    @SerializedName("alias_name")
    String alias;
    @SerializedName("featured_image")
    String coverImageUrl;
    @SerializedName("icon")
    String iconUrl;
    @SerializedName("small_icon")
    String smallIconUrl;
    @SerializedName("created_at")
    Long createdAt;
    @SerializedName("updated_At")
    Long updatedAt;
    @SerializedName("lft")
    Integer leftIndex;
    @SerializedName("rgt")
    Integer rightIndex;
    Integer depth;


    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String pCoverImageUrl) {
        coverImageUrl = pCoverImageUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String pIconUrl) {
        iconUrl = pIconUrl;
    }

    public String getSmallIconUrl() {
        return smallIconUrl;
    }

    public void setSmallIconUrl(String pSmallIconUrl) {
        smallIconUrl = pSmallIconUrl;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long pCreatedAt) {
        createdAt = pCreatedAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long pUpdatedAt) {
        updatedAt = pUpdatedAt;
    }

    public Integer getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(Integer pRightIndex) {
        rightIndex = pRightIndex;
    }

    public Integer getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(Integer pLeftIndex) {
        leftIndex = pLeftIndex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String pAlias) {
        alias = pAlias;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer pDepth) {
        depth = pDepth;
    }
}