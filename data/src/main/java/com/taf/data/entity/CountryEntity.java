package com.taf.data.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.taf.model.CountryInfo;

import java.util.List;

public class CountryEntity {

    long id;
    String title;
    @SerializedName("title_en")
    String titleEnglish;
    String description;
    @SerializedName("featured_image")
    String featuredImage;
    @SerializedName("small_icon")
    String smallIcon;
    String icon;
    List<CountryInfoEntity> information;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
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

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setInformation(List<CountryInfoEntity> information) {
        this.information = information;
    }

    public List<CountryInfoEntity> getInformation() {
        return information;
    }
}
