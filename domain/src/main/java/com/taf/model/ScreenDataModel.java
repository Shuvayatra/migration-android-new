package com.taf.model;

import java.util.List;

/**
 * Created by umesh on 1/13/17.
 */

public class ScreenDataModel<T> {
    int lastPage;
    int totalCount;
    int currentPage;
    private boolean isFromCache;
    List<T> data;

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isFromCache() {
        return isFromCache;
    }

    public void setFromCache(boolean fromCache) {
        isFromCache = fromCache;
    }

    @Override
    public String toString() {
        return "ScreenDataModel{" +
                "lastPage=" + lastPage +
                ", totalCount=" + totalCount +
                ", currentPage=" + currentPage +
                ", isFromCache=" + isFromCache +
                ", data=" + data +
                '}';
    }
}
