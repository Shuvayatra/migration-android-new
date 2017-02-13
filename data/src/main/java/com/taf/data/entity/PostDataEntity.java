package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostDataEntity {
    @SerializedName("media_url")
    String mediaUrl;
    String content;
    String thumbnail;
    String duration;
    @SerializedName("phone")
    List<String> phoneNumbers;
    String address;
    @SerializedName("photo_credit")
    String photoCredit;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String pMediaUrl) {
        mediaUrl = pMediaUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String pThumbnail) {
        thumbnail = pThumbnail;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String pDuration) {
        duration = pDuration;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> pPhoneNumbers) {
        phoneNumbers = pPhoneNumbers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String pAddress) {
        address = pAddress;
    }
}
