package com.taf.model;


import com.taf.util.MyConstants;

import java.util.List;

public class Post extends BaseModel {
    String mTitle;
    String mDescription;
    String mType;
    PostData mData;
    String mSource;
    List<String> mTags;
    Long mCreatedAt;
    Long mUpdatedAt;
    Integer likes;
    Integer share;
    String category;

    Boolean isFavourite;
    Boolean isSynced;
    Boolean downloadStatus;
    Long downloadReference;

    // for pagination purpose
    Integer mTotalCount;


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

    public List<String> getTags() {
        return mTags;
    }

    public void setTags(List<String> pTags) {
        mTags = pTags;
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

    public String getType() {
        return mType;
    }

    public void setType(String pType) {
        mType = pType;
    }

    public PostData getData() {
        return mData;
    }

    public void setData(PostData pData) {
        mData = pData;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String pSource) {
        mSource = pSource;
    }

    @Override
    public int getDataType() {
        if (getType().equalsIgnoreCase("audio")) {
            return MyConstants.Adapter.TYPE_AUDIO;
        } else if (getType().equalsIgnoreCase("video")) {
            return MyConstants.Adapter.TYPE_VIDEO;
        } else if (getType().equalsIgnoreCase("text")) {
            return MyConstants.Adapter.TYPE_TEXT;
        } else if (getType().equalsIgnoreCase("news")) {
            return MyConstants.Adapter.TYPE_NEWS;
        } else {
            return MyConstants.Adapter.TYPE_PLACE;
        }
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer pLikes) {
        likes = pLikes;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer pShare) {
        share = pShare;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String pCategory) {
        category = pCategory;
    }

    public Integer getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(Integer pTotalCount) {
        mTotalCount = pTotalCount;
    }

    public Boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean pFavourite) {
        isFavourite = pFavourite;
    }

    public Boolean isSynced() {
        return isSynced;
    }

    public void setIsSynced(Boolean pSynced) {
        isSynced = pSynced;
    }

    public Boolean getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(Boolean pDownloadStatus) {
        downloadStatus = pDownloadStatus;
    }

    public Long getDownloadReference() {
        return downloadReference;
    }

    public void setDownloadReference(Long pDownloadReference) {
        downloadReference = pDownloadReference;
    }
}
