package com.taf.model;

import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public class Category extends BaseModel {
    String name;
    String iconUrl;
    String detailImageUrl;
    String detailIconUrl;
    Long parentId;
    Long position;
    Long categoryId;
    String sectionName;

    @Override
    public int getDataType() {
        return MyConstants.Adapter.TYPE_JOURNEY_CATEGORY;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String pIconUrl) {
        iconUrl = pIconUrl;
    }

    public String getDetailImageUrl() {
        return detailImageUrl;
    }

    public void setDetailImageUrl(String pDetailImageUrl) {
        detailImageUrl = pDetailImageUrl;
    }

    public String getDetailIconUrl() {
        return detailIconUrl;
    }

    public void setDetailIconUrl(String pDetailIconUrl) {
        detailIconUrl = pDetailIconUrl;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long pCategoryId) {
        categoryId = pCategoryId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String pSectionName) {
        sectionName = pSectionName;
    }

    @Override
    public String toString() {
        return name;
    }
}
