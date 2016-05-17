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
    String featuredImage;

    Boolean isFavourite;
    Boolean isSynced;
    Boolean downloadStatus;
    Long downloadReference;
    Integer mViewCount;
    Integer mUnSyncedViewCount;
    Integer mUnSyncedShareCount;

    List<Category> mCategoryList;
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
        for (Category category : getCategoryList()) {
            if (category.getParentAlias() != null && category.getParentAlias().equals(MyConstants.SECTION
                    .JOURNEY)) {
                System.out.println(mId+" Post: journey"+ category.getTitle());
                return category.getTitle();
            }else{
                System.out.println(mId+" Post: not "+ category.getTitle());

            }
        }
        //// TODO: 5/16/2016 need proper logic
        return getCategoryList().get(0).getParentAlias()!=null?getCategoryList().get(0).getTitle():getCategoryList().get(0).getAlias();
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

    public List<Category> getCategoryList() {
        return mCategoryList;
    }

    public void setCategoryList(List<Category> pCategoryList) {
        mCategoryList = pCategoryList;
    }

    public Integer getViewCount() {
        return mViewCount;
    }

    public void setViewCount(Integer pViewCount) {
        mViewCount = pViewCount;
    }

    public Integer getUnSyncedViewCount() {
        return mUnSyncedViewCount != null ? mUnSyncedViewCount : 0;
    }

    public void setUnSyncedViewCount(Integer pUnSyncedViewCount) {
        mUnSyncedViewCount = pUnSyncedViewCount;
    }

    public String getTotalViews() {
        return getInTermOfK(mViewCount, mUnSyncedViewCount);
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String pFeaturedImage) {
        featuredImage = pFeaturedImage;
    }

    public Integer getUnSyncedShareCount() {
        return mUnSyncedShareCount != null ? mUnSyncedShareCount : 0;
    }

    public void setUnSyncedShareCount(Integer pUnSyncedShareCount) {
        mUnSyncedShareCount = pUnSyncedShareCount;
    }

    public String getTotalShare() {
        return getInTermOfK(share, mUnSyncedShareCount);
    }

    public String getTotalLikes() {
        return getInTermOfK(likes, 0);
    }

    public String getInTermOfK(Integer num1, Integer num2) {
        Integer total = ((num1 != null ? num1 : 0) + (num2 != null ? num2 : 0));
        if (total < 1000) {
            return total.toString();
        } else {
            return total / 1000 + "K";
        }
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
