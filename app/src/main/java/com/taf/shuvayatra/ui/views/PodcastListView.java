package com.taf.shuvayatra.ui.views;

import com.taf.model.Podcast;
import com.taf.model.PodcastResponse;

import java.util.List;

/**
 * Created by julian on 10/24/16.
 */

public interface PodcastListView extends LoadDataView {
    void renderPodcasts(PodcastResponse podcasts);
}
