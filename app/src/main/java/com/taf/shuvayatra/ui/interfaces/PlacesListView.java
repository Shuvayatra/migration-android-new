package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Post;

import java.util.List;

public interface PlacesListView extends LoadDataView{
    void renderPlaces(List<Post> pPlaces);
}
