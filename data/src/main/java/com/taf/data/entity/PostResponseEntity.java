package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by julian on 10/26/16.
 */

public class PostResponseEntity {
    int total;
    @SerializedName("per_page")
    int limit;
    @SerializedName("current_page")
    int currentPage;
    @SerializedName("last_page")
    int lastPage;
    List<PostEntity> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<PostEntity> getData() {
        return data;
    }

    public void setData(List<PostEntity> data) {
        this.data = data;
    }
}
