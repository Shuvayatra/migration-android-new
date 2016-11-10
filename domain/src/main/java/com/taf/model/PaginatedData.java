package com.taf.model;

import java.util.List;

/**
 * Created by julian on 10/26/16.
 */

public class PaginatedData<T> {
    int total;
    int limit;
    int currentPage;
    int lastPage;
    List<T> data;

    public PaginatedData(int currentPage, int lastPage) {
        this.currentPage = currentPage;
        this.lastPage = lastPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
