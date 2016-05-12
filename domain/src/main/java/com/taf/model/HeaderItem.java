package com.taf.model;

/**
 * Created by Nirazan-PC on 5/12/2016.
 */
public class HeaderItem extends BaseModel {
    String title;

    public HeaderItem(String pTitle) {
        title = pTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }
}
