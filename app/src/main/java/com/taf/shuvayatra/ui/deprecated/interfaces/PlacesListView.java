package com.taf.shuvayatra.ui.deprecated.interfaces;

import com.taf.model.Post;

import java.util.List;

public interface PlacesListView extends LoadDataView {
    void renderPlaces(List<Post> pPlaces);
}
