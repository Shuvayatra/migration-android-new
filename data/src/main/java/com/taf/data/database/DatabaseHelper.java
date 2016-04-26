package com.taf.data.database;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.taf.data.database.dao.DaoSession;
import com.taf.data.database.dao.DbCategory;
import com.taf.data.database.dao.DbCategoryDao;
import com.taf.data.database.dao.DbPost;
import com.taf.data.database.dao.DbPostDao;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.mapper.DataMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import de.greenrobot.dao.query.QueryBuilder;
import rx.Observable;

public class DatabaseHelper {

    private final DaoSession mDaoSession;
    private final DataMapper mDataMapper;

    @Inject
    public DatabaseHelper(DaoSession pDaoSession, DataMapper pDataMapper) {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        mDaoSession = pDaoSession;
        mDataMapper = pDataMapper;
    }

    private DbPost mapCursorToPost(Cursor pCursor, int pOffset) {
        DbPost post = new DbPost(pCursor.getLong(pCursor.getColumnIndex(DbPostDao.Properties.Id.columnName)));
        post.setTitle(pCursor.getString(pCursor.getColumnIndex(DbPostDao.Properties.Title.columnName)));
        post.setDescription(pCursor.getString(pCursor.getColumnIndex(DbPostDao.Properties
                .Description.columnName)));
        post.setType(pCursor.getString(pCursor.getColumnIndex(DbPostDao.Properties.Type
                .columnName)));
        post.setData(pCursor.getString(pCursor.getColumnIndex(DbPostDao.Properties.Data
                .columnName)));
        post.setSource(pCursor.getString(pCursor.getColumnIndex(DbPostDao.Properties.Source
                .columnName)));
        post.setTags(pCursor.getString(pCursor.getColumnIndex(DbPostDao.Properties.Tags
                .columnName)));
        post.setCreatedAt(pCursor.getLong(pCursor.getColumnIndex(DbPostDao.Properties.CreatedAt
                .columnName)));
        post.setUpdatedAt(pCursor.getLong(pCursor.getColumnIndex(DbPostDao.Properties.UpdatedAt
                .columnName)));
        post.setFavouriteCount(pCursor.getInt(pCursor.getColumnIndex(DbPostDao.Properties
                .FavouriteCount.columnName)));
        post.setShareCount(pCursor.getInt(pCursor.getColumnIndex(DbPostDao.Properties.ShareCount
                .columnName)));
        post.setIsFavourite(pCursor.getInt(pCursor.getColumnIndex(DbPostDao.Properties
                .IsFavourite.columnName)) == 1);
        post.setIsSynced(pCursor.getInt(pCursor.getColumnIndex(DbPostDao.Properties.IsSynced
                .columnName)) == 1);
        return post;
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

    public Observable<Map<String, Object>> getPosts(int pLimit, int pOffset) {
        return getPosts(pLimit, pOffset, null, false);
    }

    public Observable<Map<String, Object>> getFavouritePosts(int pLimit, int pOffset) {
        return getPosts(pLimit, pOffset, null, true);
    }

    public Observable<Map<String, Object>> getPostsByType(int pLimit, int pOffset, @NonNull String pType) {
        return getPosts(pLimit, pOffset, pType, false);
    }

    public Observable<Map<String, Object>> getPostByCategory(Long categoryId, int pLimit,
                                                             int pOffset) {
        return getPosts(pLimit, pOffset, null, false);
    }

    public Observable<Map<String, Object>> getPosts(int pLimit, int pOffset, String pType, boolean
            pFavouritesOnly) {
        Map<String, Object> map = new HashMap<>();
        DbPostDao postDao = mDaoSession.getDbPostDao();

        QueryBuilder<DbPost> queryBuilder = postDao.queryBuilder();
        if (pType != null) {
            queryBuilder.where(DbPostDao.Properties.Type.eq(pType));
        }
        if (pFavouritesOnly) {
            queryBuilder.where(DbPostDao.Properties.IsFavourite.eq(true));
        }

        map.put("total_count", queryBuilder.list().size());
        map.put("data", queryBuilder
                .limit(pLimit)
                .offset(pOffset)
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .list()
        );

        return Observable.defer(() -> Observable.just(map));
    }

    public Observable<List<DbPost>> getPostsWithUnSyncedFavourites() {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        List<DbPost> dbPosts = postDao.queryBuilder()
                .where(DbPostDao.Properties.IsSynced.eq(false))
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .list();

        return Observable.defer(() -> Observable.just(dbPosts));
    }

    public void updateFavouriteState(List<Long> pIds, boolean isSynced) {
        updateFavouriteState(pIds, null, isSynced);
    }

    public void updateFavouriteState(List<Long> pIds, Boolean isFavourite, boolean isSynced) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        List<DbPost> postList = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.in(pIds))
                .list();
        for (DbPost dbPost : postList) {
            if (isFavourite != null) {
                dbPost.setIsFavourite(isFavourite);
                dbPost.setFavouriteCount(isFavourite
                        ? dbPost.getFavouriteCount() + 1
                        : dbPost.getFavouriteCount() - 1
                );
            }
            dbPost.setIsSynced(isSynced);
        }
        postDao.insertOrReplaceInTx(postList);
    }

    public void updateFavouriteState(Long pId, Boolean isFavourite, boolean isSynced) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost post = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.eq(pId))
                .unique();
        if (isFavourite != null) {
            post.setIsFavourite(isFavourite);
            post.setFavouriteCount(isFavourite
                    ? post.getFavouriteCount() + 1
                    : post.getFavouriteCount() - 1
            );
        }
        post.setIsSynced(isSynced);
        postDao.insertOrReplace(post);
    }

    public Observable<List<DbCategory>> getCategoriesBySection(String section) {
        DbCategoryDao categoriesDao = mDaoSession.getDbCategoryDao();
        List<DbCategory> categories = categoriesDao.queryBuilder().where(DbCategoryDao.Properties.SectionName.eq(section))
                .orderAsc(DbCategoryDao.Properties.Position).list();
        return Observable.defer(() -> Observable.just(categories));
    }

    public long updateDownloadStatus(Long pReference, boolean pDownloadStatus) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost post = postDao.queryBuilder()
                .where(DbPostDao.Properties.DownloadReference.eq(pReference))
                .unique();
        post.setIsDownloaded(pDownloadStatus);
        return postDao.insertOrReplace(post);
    }

    public long setDownloadReference(Long pId, long pReference) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost post = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.eq(pId))
                .unique();
        post.setDownloadReference(pReference);
        return postDao.insertOrReplace(post);
    }

}
