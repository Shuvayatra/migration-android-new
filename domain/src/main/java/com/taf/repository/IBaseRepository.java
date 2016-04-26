package com.taf.repository;

import java.util.List;

import rx.Observable;

public interface IBaseRepository<T> {
    Observable<List<T>> getList(int pLimit, int pOffset);

    Observable<T> getSingle(Long id);
}
