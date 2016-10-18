package com.taf.shuvayatra.models;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class Block extends Item {

    private String layout;
    private String title;
    private List<Post> data;

    @Override
    public void bind(int position) {

    }

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
