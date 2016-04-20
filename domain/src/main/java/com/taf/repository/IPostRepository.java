package com.taf.repository;

import com.taf.model.Post;

import java.util.List;

import rx.Observable;

public interface IPostRepository extends IBaseRepository<Post>{
    Observable<List<Post>> getListByType(String pType, int pLimit, int pOffset);
    Observable updateFavouriteState(Long pId, boolean isFavourite);
}
