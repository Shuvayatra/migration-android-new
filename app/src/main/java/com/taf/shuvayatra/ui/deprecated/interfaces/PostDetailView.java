package com.taf.shuvayatra.ui.deprecated.interfaces;

public interface PostDetailView extends MvpView{
    void onPostFavouriteStateUpdated(Boolean status);
    void onViewCountUpdated();
    void onShareCountUpdate();
}
