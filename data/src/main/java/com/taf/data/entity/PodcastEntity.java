package com.taf.data.entity;

/**
 * Created by julian on 10/24/16.
 */

public class PodcastEntity {
    Long id;
    String title;
    String description;
    String source;
    String image;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof PodcastEntity)) return false;
        if(this.title.equals(((PodcastEntity) obj).getTitle())) return true;
        return false;
    }
}
