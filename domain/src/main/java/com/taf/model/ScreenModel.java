package com.taf.model;

/**
 * Created by umesh on 1/13/17.
 */

public class ScreenModel extends BaseModel {

    private String title;
    private String icon;
    private String endPOint;
    private String type;
    private int order;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEndPoint() {
        return endPOint;
    }

    public void setEndPOint(String endPOint) {
        this.endPOint = endPOint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
