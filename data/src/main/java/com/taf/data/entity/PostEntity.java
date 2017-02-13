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
    @SerializedName("source_url")
    String sourceUrl;
    @SerializedName("share_url")
    String shareUrl;
    List<String> tags;
    @SerializedName("section_category")
    List<Long> categoryIds;
    @SerializedName("created_at")
    Long createdAt;
    @SerializedName("updated_at")
    Long updatedAt;
    @SerializedName("featured_image")
    String featuredImage;
    @SerializedName("like_count")
    Integer favouriteCount;
    @SerializedName("share_count")
    Integer shareCount;
    @SerializedName("photo_credit")
    String photoCredit;

    @SerializedName("view_count")
    Integer viewCount;
    Integer priority;

    @SerializedName("similar_posts")
    List<PostEntity> similarPosts;

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

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

    public String getPhotoCredit() {
        return photoCredit;
    }

    public void setPhotoCredit(String photoCredit) {
        this.photoCredit = photoCredit;
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

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> pCategoryIds) {
        categoryIds = pCategoryIds;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String pFeaturedImage) {
        featuredImage = pFeaturedImage;
    }

    public Integer getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Integer pFavouriteCount) {
        favouriteCount = pFavouriteCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer pShareCount) {
        shareCount = pShareCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer pViewCount) {
        viewCount = pViewCount;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String pShareUrl) {
        shareUrl = pShareUrl;
    }

    public List<PostEntity> getSimilarPosts() {
        return similarPosts;
    }

    public void setSimilarPosts(List<PostEntity> similarPosts) {
        this.similarPosts = similarPosts;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data +
                ", source='" + source + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", tags=" + tags +
                ", categoryIds=" + categoryIds +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", featuredImage='" + featuredImage + '\'' +
                ", favouriteCount=" + favouriteCount +
                ", shareCount=" + shareCount +
                ", viewCount=" + viewCount +
                ", priority=" + priority +
                ", similarPosts=" + similarPosts +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PostEntity)) return false;
        return this.id.equals(((PostEntity) obj).getId());

    }
}
