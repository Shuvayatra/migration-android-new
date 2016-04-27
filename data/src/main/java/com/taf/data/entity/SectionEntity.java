package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SectionEntity {
    Long id;
    String title;
    @SerializedName("alias_name")
    String alias;

    @SerializedName("category")
    List<CategoryEntity> mCategoryList;
    @SerializedName("sub_category")
    List<CategoryEntity> mSubCategoryList;

    @SerializedName("created_at")
    Long createdAt;
    @SerializedName("updated_At")
    Long updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String pAlias) {
        alias = pAlias;
    }

    public List<CategoryEntity> getCategoryList() {
        return mCategoryList;
    }

    public void setCategoryList(List<CategoryEntity> pCategoryList) {
        mCategoryList = pCategoryList;
    }

    public List<CategoryEntity> getSubCategoryList() {
        return mSubCategoryList;
    }

    public void setSubCategoryList(List<CategoryEntity> pSubCategoryList) {
        mSubCategoryList = pSubCategoryList;
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
}