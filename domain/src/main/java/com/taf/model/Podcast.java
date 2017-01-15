package com.taf.model;

import com.taf.util.MyConstants;

/**
 * Created by julian on 10/24/16.
 */

public class Podcast extends BaseModel {
    String title;
    String description;
    String source;
    String image;

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
    public int getDataType() {
        return MyConstants.Adapter.TYPE_PODCAST;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Podcast)) {
            return false;
        }

        Podcast podcast = (Podcast) o;
        return podcast.title != null && getTitle().equalsIgnoreCase(podcast.title);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", source='" + source + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    public static Podcast convertPost(Post post) {
        Podcast podcast = new Podcast();
        podcast.title = post.getTitle();
        podcast.image = post.getData().getThumbnail();
        podcast.source = post.getData().getMediaUrl();
        podcast.description = post.getDescription();
        return podcast;
    }
}
