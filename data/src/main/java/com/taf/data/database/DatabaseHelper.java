package com.taf.data.database;

import android.support.annotation.NonNull;

import com.taf.data.database.dao.DaoSession;
import com.taf.data.database.dao.DbPost;
import com.taf.data.database.dao.DbPostDao;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.mapper.DataMapper;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class DatabaseHelper {

    private final DaoSession mDaoSession;
    private final DataMapper mDataMapper;

    @Inject
    public DatabaseHelper(DaoSession pDaoSession, DataMapper pDataMapper) {
        mDaoSession = pDaoSession;
        mDataMapper = pDataMapper;
    }

    private <T> Observable<T> makeObservable(final Callable<T> callable) {

        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(callable.call());
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                });
    }

    public void clearCache(DaoSession pDaoSession) {
        pDaoSession.clear();
    }

    public void insertUpdate(LatestContentEntity pEntity) {
        insertUpdate(pEntity.getPosts());
    }

    public void insertUpdate(List<PostEntity> pEntities) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        for (PostEntity entity : pEntities) {
            DbPost post = mDataMapper.transformPostForDB(entity);
            if (post != null) {
                postDao.insertOrReplace(post);
            }
        }
    }

    public Observable<DbPost> getPost(Long pId) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost dbPost = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.eq(pId))
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .unique();
        return Observable.defer(() -> Observable.just(dbPost));
    }

    public Observable<List<DbPost>> getPosts(int pLimit, int pOffset) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        List<DbPost> dbPosts = postDao.queryBuilder()
                .offset(pOffset)
                .limit(pLimit)
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .list();

        return Observable.defer(() -> Observable.just(dbPosts));
    }

    public Observable<List<DbPost>> getPosts(int pLimit, int pOffset, @NonNull String pType) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        List<DbPost> dbPosts = postDao.queryBuilder()
                .offset(pOffset)
                .limit(pLimit)
                .where(DbPostDao.Properties.Type.eq(pType))
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .list();

        return Observable.defer(() -> Observable.just(dbPosts));
    }

    public Observable<List<DbPost>> getUnSyncedPosts() {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        List<DbPost> dbPosts = postDao.queryBuilder()
                .where(DbPostDao.Properties.IsSynced.eq(false))
                .where(DbPostDao.Properties.IsFavourite.eq(true))
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .list();

        return Observable.defer(() -> Observable.just(dbPosts));
    }

    public Long updateFavouriteState(Long pId, boolean isFavourite, boolean isSynced){
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost dbPost = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.eq(pId))
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .unique();
        dbPost.setIsFavourite(isFavourite);
        dbPost.setIsSynced(isSynced);
        return postDao.insertOrReplace(dbPost);
    }

}
