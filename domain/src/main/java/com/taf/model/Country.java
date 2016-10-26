package com.taf.model;

import java.util.Locale;

/**
 * Created by rakeeb on 10/25/16.
 */

public class Country extends BaseModel {

    /**
     * use {@link #getId()} and {@link #setId(Long)} for id
     */

    String title;
    String description;
    String featuredImage;
    String icon;
    String smallIcon;

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

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "id: %d, title: %s", getId(), getTitle());
    }
}
