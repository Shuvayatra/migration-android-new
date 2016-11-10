package com.taf.model;

/**
 * Created by julian on 10/26/16.
 */

public class PodcastResponse {
    Long id;
    String title;
    PaginatedData<Podcast> data;

    public PodcastResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PaginatedData<Podcast> getData() {
        return data;
    }

    public void setData(PaginatedData<Podcast> data) {
        this.data = data;
    }
}
