package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by umesh on 1/12/17.
 */

public class PodcastFeed {

    private int total;
    @SerializedName("per_page")
    private int perPage;

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("last_page")
    private int lastPage;

    @SerializedName("next_page_url")
    private String nextPageUrl;

    @SerializedName("previous_page_url")
    private String prevoiusPageUrl;

    private List<PodcastEntity> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
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

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPrevoiusPageUrl() {
        return prevoiusPageUrl;
    }

    public void setPrevoiusPageUrl(String prevoiusPageUrl) {
        this.prevoiusPageUrl = prevoiusPageUrl;
    }

    public List<PodcastEntity> getData() {
        return data;
    }

    public void setData(List<PodcastEntity> data) {
        this.data = data;
    }
}
