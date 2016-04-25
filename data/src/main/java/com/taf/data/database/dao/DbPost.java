package com.taf.data.database.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "DB_POST".
 */
public class DbPost {

    private Long id;
    private String title;
    private String description;
    private String type;
    private String data;
    private String source;
    private String tags;
    private Long createdAt;
    private Long updatedAt;
    private Integer favouriteCount;
    private Integer shareCount;
    private Boolean isFavourite;
    private Boolean isSynced;

    public DbPost() {
    }

    public DbPost(Long id) {
        this.id = id;
    }

    public DbPost(Long id, String title, String description, String type, String data, String source, String tags, Long createdAt, Long updatedAt, Integer favouriteCount, Integer shareCount, Boolean isFavourite, Boolean isSynced) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.data = data;
        this.source = source;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.favouriteCount = favouriteCount;
        this.shareCount = shareCount;
        this.isFavourite = isFavourite;
        this.isSynced = isSynced;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Integer favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Boolean getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(Boolean isSynced) {
        this.isSynced = isSynced;
    }

}
