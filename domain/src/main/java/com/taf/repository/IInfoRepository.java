package com.taf.repository;

import com.taf.model.Info;

import rx.Observable;

/**
 * Created by yipl on 1/16/17.
 */

public interface IInfoRepository {
    Observable<Info> getInfo(String key);
}
