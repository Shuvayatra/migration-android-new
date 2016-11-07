package com.taf.model;

import com.taf.util.MyConstants;

/**
 * Created by ngima on 11/3/16.
 */

public class Channel extends BaseModel {


    String title;
    String description;

    @Override
    public int getDataType() {
        return super.getDataType() == 0? MyConstants.Adapter.TYPE_CHANNEL: super.getDataType();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
