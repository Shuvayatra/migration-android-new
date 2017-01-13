package com.taf.model;

import com.taf.util.MyConstants;

public class CountryWidgetModel extends BaseModel {

    private String mCountryName;
    private String mCountryNameEn;
    private String weather;
    private String temperature;
    private String mEnglishDate;
    private String mNepaliDate;
    private String mForex;
    private int mImageresource = Integer.MIN_VALUE;
    String icon;

    @Override
    public int getDataType() {
        return mDataType == 0 ? MyConstants.Adapter.TYPE_COUNTRY_WIDGET : mDataType;
    }

    public CountryWidgetModel(String countryName, String countryNameEn) {
        mCountryName = countryName;
        mCountryNameEn = countryNameEn;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getCountryNameEn() {
        return mCountryNameEn;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }

    public String getEnglishDate() {
        return mEnglishDate;
    }

    public void setEnglishDate(String englishDate) {
        mEnglishDate = englishDate;
    }

    public String getNepaliDate() {
        return mNepaliDate;
    }

    public void setNepaliDate(String nepaliDate) {
        mNepaliDate = nepaliDate;
    }

    public String getForex() {
        return mForex;
    }

    public void setForex(String forex) {
        mForex = forex;
    }

    public int getImageResource() {
        return mImageresource;
    }

    public void setImageResource(int imageresource) {
        mImageresource = imageresource;
    }
}
