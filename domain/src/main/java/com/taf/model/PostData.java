package com.taf.model;

import java.io.Serializable;

public class PostData implements Serializable {
    String mMediaUrl;
    String mContent;
    String mThumbnail;
    String mDuration;

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(String pMediaUrl) {
        mMediaUrl = pMediaUrl;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String pContent) {
        mContent = pContent;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String pThumbnail) {
        mThumbnail = pThumbnail;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String pDuration) {
        mDuration = pDuration;
    }
}
