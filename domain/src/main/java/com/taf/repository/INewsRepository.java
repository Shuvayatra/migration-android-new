package com.taf.repository;

import com.taf.model.Block;
import com.taf.model.Post;
import com.taf.model.PostResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by julian on 10/18/16.
 */
@Deprecated
public interface INewsRepository {
    Observable<PostResponse> getNewsList(boolean noCache, int page);
}
