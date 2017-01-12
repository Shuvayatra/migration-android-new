package com.taf.repository;

import com.taf.model.Post;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by umesh on 1/11/17.
 */

public interface IUserAccountRepository {
    public Observable<List<Post>> getFavouritePost();
}
