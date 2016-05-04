package com.taf.shuvayatra.ui.interfaces;

public interface PostDetailView extends MvpView{
    void onPostFavouriteStateUpdated(Boolean status);
    void onViewCountUpdated();
}
