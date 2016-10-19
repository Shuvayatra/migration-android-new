package com.taf.model;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class Block {
    private String layout;
    private String title;
    private List<Post> data;

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }
}
