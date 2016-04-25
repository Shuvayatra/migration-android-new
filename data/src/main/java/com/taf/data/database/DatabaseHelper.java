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
import com.taf.data.utils.Logger;

import java.util.ArrayList;
import java.util.List;

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
        try {
            post.setCurrentOffset(pOffset);
            post.setTotalCount(pCursor.getInt(pCursor.getColumnIndexOrThrow("total_count")));
        } catch (Exception e) {
            post.setTotalCount(0);
        }
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

    public Observable<List<DbPost>> getPosts(int pLimit, int pOffset) {
        return getPosts(pLimit, pOffset, null, false);
    }

    public Observable<List<DbPost>> getFavouritePosts(int pLimit, int pOffset) {
        return getPosts(pLimit, pOffset, null, true);
    }

    public Observable<List<DbPost>> getPostsByType(int pLimit, int pOffset, @NonNull String pType) {
        return getPosts(pLimit, pOffset, pType, false);
    }

    public Observable<List<DbPost>> getPostByCategory(Long categoryId,int pLimit, int pOffset){
        return getPosts(pLimit,pOffset, null,false);
    }

    public Observable<List<DbPost>> getPosts(int pLimit, int pOffset, String pType, boolean
            pFavouritesOnly) {
        List<DbPost> dbPosts = new ArrayList<>();
        String where = " where 1 ";
        if (pFavouritesOnly) {
            where += " and is_favourite = 1 ";
        }
        if (pType != null) {
            where += " and type = '" + pType + "' ";
        }
        String subQuery = " (select count(*) from db_post "+ where +" ) ";
        String sql = "select "+ subQuery +" total_count, p.* from db_post p ";
        sql += where + " order by created_at desc limit " + pLimit + " offset " + (pOffset *
                pLimit);
        Logger.d("DatabaseHelper_getPosts", "sql: " + sql);
        Cursor c = mDaoSession.getDatabase().rawQuery(sql, null);
        try {
            if (c.moveToFirst()) {
                do {
                    dbPosts.add(mapCursorToPost(c, pOffset));
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
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

    public Long updateFavouriteState(Long pId, boolean isFavourite, boolean isSynced) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost dbPost = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.eq(pId))
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .unique();
        dbPost.setIsFavourite(isFavourite);
        dbPost.setIsSynced(isSynced);
        return postDao.insertOrReplace(dbPost);
    }

    public Observable<List<DbCategory>> getCategoriesBySection(String section) {
        DbCategoryDao categoriesDao = mDaoSession.getDbCategoryDao();
        List<DbCategory> categories = categoriesDao.queryBuilder().where(DbCategoryDao.Properties.SectionName.eq(section))
                .orderAsc(DbCategoryDao.Properties.Position).list();
        Logger.e("DatabaseHelper", "categories: "+categories);
        return Observable.defer(() -> Observable.just(categories));
    }

}
