package com.taf.data.entity;

import java.util.List;

/**
 *  Entity to hold data from the endpoint of screen
 */

public class ScreenBlockEntity {

    private List<BlockEntity> data;


    public void setData(List<BlockEntity> data) {
        this.data = data;
    }

    public List<BlockEntity> getData() {
        return data;
    }
}
