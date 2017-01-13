package com.taf.repository;

import com.taf.model.Post;
import com.taf.model.PostResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by julian on 10/18/16.
 */

public interface IPostRepository {
    Observable<PostResponse> getList(int feedType, int limit, int offset, String filterParams);

    Observable<Post> getDetail(Long id);

    Observable<Boolean> updateFavouriteCount(Post post, boolean status);

    Observable<Boolean> updateShareCount(Long id);

    Observable<Boolean> syncUserActions();

    Observable<PostResponse> getSearchPosts(int limit, int offset, String query, String type);
}
