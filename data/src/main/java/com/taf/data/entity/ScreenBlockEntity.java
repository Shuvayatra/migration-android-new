package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  Entity to hold data from the endpoint of screen
 */

public class ScreenBlockEntity {

    private String title;
    private String name;
    @SerializedName("icon_image")
    private String icon;
    private String type;
    private List<BlockEntity> blocks;


    public void setData(List<BlockEntity> data) {
        this.blocks = data;
    }

    public List<BlockEntity> getData() {
        return blocks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ScreenBlockEntity{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", data=" + blocks +
                '}';
    }
}
