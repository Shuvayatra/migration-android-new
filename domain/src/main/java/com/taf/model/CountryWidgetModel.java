package com.taf.model;

import com.taf.util.MyConstants;

import java.util.TimeZone;

public class CountryWidgetModel extends BaseModel {

    private String mCountryName;
    private String mTimeZoneId;
    private String weather;
    private String temperature;
    private String mEnglishDate;
    private String mNepaliDate;
    private String mForex;
    private int mImageresource = Integer.MIN_VALUE;
    String icon;
    private TimeZone mTimeZone;

    @Override
    public int getDataType() {
        return mDataType == 0 ? MyConstants.Adapter.TYPE_COUNTRY_WIDGET : mDataType;
    }

    public CountryWidgetModel(String countryName) {
        mCountryName = countryName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
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

    public String getTimeZoneId() {
        return mTimeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.mTimeZoneId = timeZoneId;
        this.mTimeZone = TimeZone.getTimeZone(timeZoneId);
    }

    public TimeZone getTimeZone() {
        return mTimeZone;
    }
}
