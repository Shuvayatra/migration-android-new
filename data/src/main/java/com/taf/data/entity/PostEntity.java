package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostEntity {
    Long id;
    String title;
    String description;
    String type;
    PostDataEntity data;
    String source;
    List<String> tags;
    @SerializedName("created_at")
    Long createdAt;
    @SerializedName("updated_at")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String pType) {
        type = pType;
    }

    public PostDataEntity getData() {
        return data;
    }

    public void setData(PostDataEntity pData) {
        data = pData;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String pSource) {
        source = pSource;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> pTags) {
        tags = pTags;
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
