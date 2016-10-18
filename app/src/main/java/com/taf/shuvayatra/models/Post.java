package com.taf.shuvayatra.models;

/**
 * Created by julian on 10/18/16.
 */

public class Post extends Item {
    String mTitle;
    String mDescription;
    String mType;

    @Override
    public void bind(int position) {

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
