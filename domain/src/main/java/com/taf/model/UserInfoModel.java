package com.taf.model;


/**
 * Created by umesh on 1/12/17.
 */

public class UserInfoModel extends BaseModel {

    public static final String EXTRA_INFO_MODEL = "user_info_model";

    private String name;
    private String destinedCountry;
    private String workStatus;
    private String gender;
    private long birthday = Long.MIN_VALUE;
    private String originalLocation;

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

    public String getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(String originalLocation) {
        this.originalLocation = originalLocation;
    }

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "name='" + name + '\'' +
                ", destinedCountry='" + destinedCountry + '\'' +
                ", workStatus='" + workStatus + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", originalLocation='" + originalLocation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfoModel that = (UserInfoModel) o;

        if (birthday != that.birthday) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (destinedCountry != null ? !destinedCountry.equals(that.destinedCountry) : that.destinedCountry != null)
            return false;
        if (workStatus != null ? !workStatus.equals(that.workStatus) : that.workStatus != null)
            return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        return originalLocation != null ? originalLocation.equals(that.originalLocation) : that.originalLocation == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (destinedCountry != null ? destinedCountry.hashCode() : 0);
        result = 31 * result + (workStatus != null ? workStatus.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (int) (birthday ^ (birthday >>> 32));
        result = 31 * result + (originalLocation != null ? originalLocation.hashCode() : 0);
        return result;
    }
}
