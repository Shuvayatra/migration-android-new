package com.taf.model;


import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by julian on 12/8/15.
 */
public class Post extends BaseModel {
    Long mCreatedAt;
    Long mUpdatedAt;
    List<String> mTags;
    String mDescription;
    String mTitle;
    String type;

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
        return type;
    }

    public void setType(String pType) {
        type = pType;
    }

    @Override
    public int getDataType() {
        if (getType().equalsIgnoreCase("audio")) {
            return MyConstants.Adapter.TYPE_AUDIO;
        } else if (getType().equalsIgnoreCase("video")) {
            return MyConstants.Adapter.TYPE_VIDEO;
        } else if (getType().equalsIgnoreCase("text")) {
            return MyConstants.Adapter.TYPE_TEXT;
        }else{
            return MyConstants.Adapter.TYPE_NEWS;
        }
    }
}
