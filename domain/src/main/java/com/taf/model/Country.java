package com.taf.model;

import com.taf.util.MyConstants;

import java.util.HashMap;
import java.util.List;
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
    String titleEnglish;
    List<CountryInfo> informations;

    @Override
    public int getDataType() {
        System.out.println("country data type: "+ mDataType);
        return super.getDataType() == 0? MyConstants.Adapter.TYPE_COUNTRY: super.getDataType();
    }

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

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%d,%s", getId(), getTitle());
    }

    public List<CountryInfo> getInformations() {
        return informations;
    }

    public void setInformations(List<CountryInfo> informations) {
        this.informations = informations;
    }

    // if information is avilable gets the first key and value to show for the selected country in country list screen
    public String getFirstInformation(){
        if(!informations.isEmpty()) {
           CountryInfo info = informations.get(0);
            return  info.getAttribute() +" : "+ info.getValue();
        }
        return "";
    }

    public String getAllInformations(){
        String info = "";
        System.out.println("informations = " + informations.isEmpty());
            for (CountryInfo countryInfo  : informations) {
                info += countryInfo.getAttribute()+" : " + countryInfo.getValue() + "\n";
            }
        return info;
    }

}