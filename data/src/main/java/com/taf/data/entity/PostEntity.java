package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by julian on 12/8/15.
 */
public class PostEntity {
    Long id;
    @SerializedName("created_at")
    Long createdAt;
    @SerializedName("updated_at")
    Long updatedAt;
    List<String> tags;
    String type;
    String description;
    String title;

    long downloadReference;
    boolean downloadStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> pTags) {
        tags = pTags;
    }

    public String getType() {
        return type;
    }

    public void setType(String pType) {
        type = pType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public long getDownloadReference() {
        return downloadReference;
    }

    public void setDownloadReference(long pDownloadReference) {
        downloadReference = pDownloadReference;
    }

    public boolean isDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(boolean pDownloadStatus) {
        downloadStatus = pDownloadStatus;
    }
}
