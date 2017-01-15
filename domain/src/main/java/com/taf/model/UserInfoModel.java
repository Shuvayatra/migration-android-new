package com.taf.model;


/**
 * Created by umesh on 1/12/17.
 */

public class UserInfoModel extends BaseModel {
    private String name;
    private String destinedCountry;
    private String workStatus;
    private String gender;
    private long birthday;
    private String orignalLocation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestinedCountry() {
        return destinedCountry;
    }

    public void setDestinedCountry(String destinedCountry) {
        this.destinedCountry = destinedCountry;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getOrignalLocation() {
        return orignalLocation;
    }

    public void setOrignalLocation(String orignalLocation) {
        this.orignalLocation = orignalLocation;
    }

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "name='" + name + '\'' +
                ", destinedCountry='" + destinedCountry + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", orignalLocation='" + orignalLocation + '\'' +
                '}';
    }
}
