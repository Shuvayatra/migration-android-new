package com.taf.model;

import com.taf.util.MyConstants;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rakeeb on 10/25/16.
 */

public class Country extends BaseModel {

    /**
     * index for values stored in preference
     */
    public static final int INDEX_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_TITLE_EN = 2;
    public static final int INDEX_TIME_ZONE = 3;
    public static final int INDEX_INFO = 4;

    String title;
    String description;
    String featuredImage;
    String icon;
    String smallIcon;
    String titleEnglish;
    String timeZoneId;
    List<CountryInfo> informations;

    @Override
    public int getDataType() {
        System.out.println("country data type: " + mDataType);
        return super.getDataType() == 0 ? MyConstants.Adapter.TYPE_COUNTRY : super.getDataType();
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

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%d,%s,%s,%s", getId(), getTitle(), getTitleEnglish(), getTimeZoneId());
    }

    public List<CountryInfo> getInformation() {
        return informations;
    }

    public void setInformation(List<CountryInfo> information) {
        this.informations = information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (title != null ? !title.equals(country.title) : country.title != null) return false;
        if (titleEnglish != null ? !titleEnglish.equals(country.titleEnglish) : country.titleEnglish != null)
            return false;
        return timeZoneId != null ? timeZoneId.equals(country.timeZoneId) : country.timeZoneId == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (titleEnglish != null ? titleEnglish.hashCode() : 0);
        result = 31 * result + (timeZoneId != null ? timeZoneId.hashCode() : 0);
        return result;
    }

    // if information is avilable gets the first key and value to show for the selected country in country list screen
    public String getFirstInformation() {
        if (!informations.isEmpty()) {
            CountryInfo info = informations.get(0);
            return info.getAttribute() + " : " + info.getValue();
        }
        return "";
    }

    public String getAllInformation() {
        String info = "";
        for (CountryInfo countryInfo : informations) {
            info += countryInfo.getAttribute() + " : " + countryInfo.getValue() + "\n";
        }
        return info;
    }

    public static Country makeCountryFromPreference(String preference) {
        Country country = new Country();
        String[] data = preference.split(",");
        country.id = data.length > 0 ? Long.valueOf(data[INDEX_ID]) : Long.MIN_VALUE;
        country.title = data.length >= 2 ? data[INDEX_TITLE] : null;
        country.titleEnglish = data.length >= 3 ? data[INDEX_TITLE_EN] : null;
        country.timeZoneId = data.length >= 4 ? data[INDEX_TIME_ZONE] : null;
        return country;
    }

}
