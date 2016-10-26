package com.taf.model;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class Block {
    private int mOrder;
    private String mLayout;
    private String mTitle;
    private String mDescription;
    private List<Post> mData;
    private Notice mNotice;
    private boolean showViewMore;
    private String viewMoreTitle;
    private String deeplink;

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        this.mOrder = order;
    }

    public String getLayout() {
        return mLayout;
    }

    public void setLayout(String layout) {
        this.mLayout = layout;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public List<Post> getData() {
        return mData;
    }

    public void setData(List<Post> data) {
        this.mData = data;
    }

    public Notice getNotice() {
        return mNotice;
    }

    public void setNotice(Notice notice) {
        mNotice = notice;
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
}
