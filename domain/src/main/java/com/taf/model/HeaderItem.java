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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeaderItem that = (HeaderItem) o;

        return title != null ? title.equals(that.title) : that.title == null;

    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
