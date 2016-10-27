package com.taf.model;

import com.taf.util.MyConstants;

/**
 * Created by julian on 10/24/16.
 */

public class Podcast extends BaseModel {
    String title;
    String description;
    String source;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int getDataType() {
        return MyConstants.Adapter.TYPE_PODCAST;
    }
}
