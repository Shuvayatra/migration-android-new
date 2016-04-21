package com.taf.data.repository;

import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.model.Post;
import com.taf.repository.IPostRepository;

import java.util.List;

import rx.Observable;

public class PostRepository implements IPostRepository {

    private final DataStoreFactory mDataStoreFactory;
    private final DataMapper mDataMapper;

    public PostRepository(DataStoreFactory pDataStoreFactory, DataMapper pDataMapper) {
        mDataMapper = pDataMapper;
        mDataStoreFactory = pDataStoreFactory;
    }

    @Override
    public Observable<List<Post>> getListByType(String pType, int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getPosts(pLimit, pOffset, pType)
                .map(pPosts -> mDataMapper.transformPostFromDb(pPosts));
    }

    @Override
    public Observable<List<Post>> getList(int pLimit, int pOffset) {
        return mDataStoreFactory.createDBDataStore()
                .getPosts(pLimit, pOffset, null)
                .map(pPosts -> mDataMapper.transformPostFromDb(pPosts));
    }

    @Override
    public Observable<Post> getSingle(Long pId) {
        return mDataStoreFactory.createDBDataStore()
                .getPost(pId)
                .map(pPost -> mDataMapper.transformPostFromDb(pPost));
    }

    @Override
    public Observable updateFavouriteState(Long pId, boolean isFavourite) {
        return mDataStoreFactory.createRestDataStore()
                .updateFavouriteState(pId, isFavourite);
    }
}
