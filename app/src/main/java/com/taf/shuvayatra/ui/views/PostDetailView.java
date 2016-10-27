package com.taf.shuvayatra.ui.views;

public interface PostDetailView extends MvpView {
    void onPostFavouriteStateUpdated(Boolean status);
    void onViewCountUpdated();
    void onShareCountUpdate();
}
