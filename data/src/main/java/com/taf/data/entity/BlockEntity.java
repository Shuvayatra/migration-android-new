package com.taf.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlockEntity {
    private int position;
    private String layout;
    private String title;
    private String description;
    @SerializedName("view_more")
    private boolean showViewMore;
    @SerializedName("view_more_title")
    private String viewMoreTitle;
    @SerializedName("view_more_deeplink")
    private String deeplink;
    @SerializedName("view_more_filter")
    private List<Long> filterIds;

    @SerializedName("content")
    private List<PostEntity> data;
    private NoticeEntity notice;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
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

    public List<PostEntity> getData() {
        return data;
    }

    public void setData(List<PostEntity> data) {
        this.data = data;
    }

    public NoticeEntity getNotice() {
        return notice;
    }

    public void setNotice(NoticeEntity notice) {
        this.notice = notice;
    }

    public boolean isShowViewMore() {
        return showViewMore;
    }

    public void setShowViewMore(boolean showViewMore) {
        this.showViewMore = showViewMore;
    }

    public String getViewMoreTitle() {
        return viewMoreTitle;
    }

    public void setViewMoreTitle(String viewMoreTitle) {
        this.viewMoreTitle = viewMoreTitle;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public List<Long> getFilterIds() {
        return filterIds;
    }

    public void setFilterIds(List<Long> filterIds) {
        this.filterIds = filterIds;
    }
}
