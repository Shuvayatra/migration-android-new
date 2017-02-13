package com.taf.model;


import com.taf.util.MyConstants;

import java.util.List;
import java.util.Locale;

public class Post extends BaseModel {

    String mTitle;
    String mDescription;
    String mType;
    PostData mData;
    String mSource;
    String shareUrl;
    List<String> mTags;
    Long mCreatedAt;
    Long mUpdatedAt;
    Integer likes;
    Integer share;
    String featuredImage;
    String photoCredit;

    Boolean isFavourite;
    Boolean isSynced;
    Boolean downloadStatus;
    Long downloadReference;
    Integer mViewCount;
    Integer mUnSyncedViewCount;
    Integer mUnSyncedShareCount;

    String sourceUrl;

    String mCategory;
    // for pagination purpose
    Integer mTotalCount;
    int priority;

    List<Post> mSimilarPosts;

    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_PLACE = "place";
    public static final String TYPE_NEWS = "news";

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

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
        if (getType().equalsIgnoreCase(TYPE_AUDIO)) {
            return MyConstants.Adapter.TYPE_AUDIO;
        } else if (getType().equalsIgnoreCase(TYPE_VIDEO)) {
            return MyConstants.Adapter.TYPE_VIDEO;
        } else if (getType().equalsIgnoreCase(TYPE_TEXT)) {
            return MyConstants.Adapter.TYPE_TEXT;
        } else if (getType().equalsIgnoreCase(TYPE_NEWS)) {
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
        return mCategory;
    }

    public void setCategory(String pCategory) {
        mCategory = pCategory;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String pShareUrl) {
        shareUrl = pShareUrl;
    }

    public List<Post> getSimilarPosts() {
        return mSimilarPosts;
    }

    public void setSimilarPosts(List<Post> similarPosts) {
        mSimilarPosts = similarPosts;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Post{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mType='" + mType + '\'' +
                ", mData=" + mData +
                ", mSource='" + mSource + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", mTags=" + mTags +
                ", mCreatedAt=" + mCreatedAt +
                ", mUpdatedAt=" + mUpdatedAt +
                ", likes=" + likes +
                ", share=" + share +
                ", featuredImage='" + featuredImage + '\'' +
                ", isFavourite=" + isFavourite +
                ", isSynced=" + isSynced +
                ", downloadStatus=" + downloadStatus +
                ", downloadReference=" + downloadReference +
                ", mViewCount=" + mViewCount +
                ", mUnSyncedViewCount=" + mUnSyncedViewCount +
                ", mUnSyncedShareCount=" + mUnSyncedShareCount +
                ", mCategory='" + mCategory + '\'' +
                ", mTotalCount=" + mTotalCount +
                ", mSimilarPosts=" + mSimilarPosts +
                '}';
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Post)) return false;
        if (this.id.equals(((Post) o).getId())) return true;
        else return false;
    }

    public String getPhotoCredit() {
        return photoCredit;
    }

    public void setPhotoCredit(String photoCredit) {
        this.photoCredit = photoCredit;
    }
}
