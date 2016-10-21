package com.taf.repository;

import com.taf.model.PostResponse;

import rx.Observable;

/**
 * Created by julian on 10/18/16.
 */

public interface IPostRepository {
    Observable<PostResponse> getList(int limit, int offset, String filterParams);
}
