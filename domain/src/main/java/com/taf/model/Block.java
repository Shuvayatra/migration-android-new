package com.taf.model;

import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */
public class Block extends BaseModel {

    public static final String TYPE_LIST = "list";
    public static final String TYPE_SLIDER = "slider";
    public static final String TYPE_NOTICE = "notice";
    public static final String TYPE_RADIO = "radio_widget";

    private int mPosition;
    private String mLayout;
    private String mTitle;
    private String mDescription;
    private List<Post> mData;
    private Notice mNotice;
    private boolean showViewMore;
    private String viewMoreTitle;
    private String deeplink;
    private List<Long> filterIds;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public String getLayout() {
        return mLayout;
    }

    public void setLayout(String layout) {
        this.mLayout = layout;
    }

    @Override
    public int getPriority() {
        switch (getLayout()) {
            case TYPE_LIST:
            case TYPE_SLIDER:
            case TYPE_RADIO:
                return 0;
            case TYPE_NOTICE:
                return 1;
            default:
                return -1;
        }
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
        return showViewMore && !deeplink.isEmpty();
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

    @Override
    public String toString() {
        return "Block{" +
                "mLayout='" + mLayout + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mNotice=" + mNotice +
                '}';
    }

    @Override
    public int getDataType() {
        return MyConstants.Adapter.TYPE_BLOCK;
    }
}
