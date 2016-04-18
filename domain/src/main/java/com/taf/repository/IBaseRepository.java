package com.taf.repository;

import java.util.List;

import rx.Observable;

public interface IBaseRepository<T> {
    Observable<List<T>> getList(int pLimit);
    Observable<T> getSingle(Long id);
}
