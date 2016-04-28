package com.taf.data.database;

import android.support.annotation.NonNull;

import com.taf.data.database.dao.DaoSession;
import com.taf.data.database.dao.DbCategory;
import com.taf.data.database.dao.DbCategoryDao;
import com.taf.data.database.dao.DbNotification;
import com.taf.data.database.dao.DbNotificationDao;
import com.taf.data.database.dao.DbPost;
import com.taf.data.database.dao.DbPostCategory;
import com.taf.data.database.dao.DbPostCategoryDao;
import com.taf.data.database.dao.DbPostDao;
import com.taf.data.database.dao.DbSection;
import com.taf.data.database.dao.DbSectionDao;
import com.taf.data.entity.CategoryEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.SectionEntity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.model.Notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import de.greenrobot.dao.query.Join;
import de.greenrobot.dao.query.QueryBuilder;
import rx.Observable;

public class DatabaseHelper {

    private final DaoSession mDaoSession;
    private final DataMapper mDataMapper;

    @Inject
    public DatabaseHelper(DaoSession pDaoSession, DataMapper pDataMapper) {
        /*QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;*/
        mDaoSession = pDaoSession;
        mDataMapper = pDataMapper;
    }

    public void clearCache(DaoSession pDaoSession) {
        pDaoSession.clear();
    }

    public void insertUpdate(LatestContentEntity pEntity) {
        insertUpdatePosts(pEntity.getPosts());
        insertUpdateSections(pEntity.getSections());
    }

    public void insertUpdatePosts(List<PostEntity> pEntities) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        for (PostEntity entity : pEntities) {
            DbPost post = mDataMapper.transformPostForDB(entity);
            if (post != null) {
                postDao.insertOrReplace(post);
            }
        }
    }

    public void insertUpdateSections(List<SectionEntity> pEntities) {
        DbSectionDao sectionDao = mDaoSession.getDbSectionDao();
        for (SectionEntity entity : pEntities) {
            DbSection section = mDataMapper.transformSectionForDB(entity);
            if (section != null) {
                sectionDao.insertOrReplace(section);
            }
            insertUpdateCategories(entity.getCategoryList(), entity.getId());
            insertUpdateCategories(entity.getSubCategoryList(), entity.getId());
        }
    }

    public void insertUpdateCategories(List<CategoryEntity> pEntities, Long pSectionId) {
        DbCategoryDao categoryDao = mDaoSession.getDbCategoryDao();
        for (CategoryEntity entity : pEntities) {
            DbCategory category = mDataMapper.transformCategoryForDB(entity);
            category.setSectionId(pSectionId);
            if (category != null) {
                categoryDao.insertOrReplace(category);
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

    public Observable<Map<String, Object>> getPostByCategory(Long categoryId, int pLimit, int
            pOffset, String pType, List<Long> excludeIds) {
        return getPosts(pLimit, pOffset, pType, false, categoryId, excludeIds);
    }

    public Observable<Map<String, Object>> getPosts(int pLimit, int pOffset, String pType, boolean
            pFavouritesOnly) {
        return getPosts(pLimit, pOffset, pType, pFavouritesOnly, null, null);
    }

    public Observable<Map<String, Object>> getPosts(int pLimit, int pOffset, String pType, boolean
            pFavouritesOnly, Long pCategoryId, List<Long> pExcludeIds) {
        Map<String, Object> map = new HashMap<>();
        DbPostDao postDao = mDaoSession.getDbPostDao();

        QueryBuilder<DbPost> queryBuilder = postDao.queryBuilder();
        if (pType != null) {
            queryBuilder.where(DbPostDao.Properties.Type.eq(pType));
        }
        if (pFavouritesOnly) {
            queryBuilder.where(DbPostDao.Properties.IsFavourite.eq(true));
        }
        if (pExcludeIds != null) {
            queryBuilder.where(DbPostDao.Properties.Id.notIn(pExcludeIds));
        }

        Join postJoin = queryBuilder.join(DbPostDao.Properties.Id, DbPostCategory.class,
                DbPostCategoryDao.Properties.PostId);
        Join categoryJoin = queryBuilder.join(postJoin, DbPostCategoryDao.Properties.CategoryId,
                DbCategory.class, DbCategoryDao.Properties.Id);
        if (pCategoryId != null) {
            categoryJoin.where(DbCategoryDao.Properties.Id.eq(pCategoryId));
        }

        List<DbPost> dbPosts = queryBuilder
                .limit(pLimit)
                .offset(pOffset)
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .distinct()
                .list();

        for (DbPost dbPost : dbPosts) {
            QueryBuilder<DbCategory> queryBuilder1 = mDaoSession.getDbCategoryDao().queryBuilder();
            queryBuilder1.join(DbCategoryDao.Properties.Id, DbPostCategory.class,
                    DbPostCategoryDao.Properties.CategoryId)
                    .where(DbPostCategoryDao.Properties.PostId.eq(dbPost.getId()));
            dbPost.setCategoryList(
                    queryBuilder1.orderAsc(DbCategoryDao.Properties.Position)
                            .list()
            );
        }

        map.put("total_count", queryBuilder.list().size());
        map.put("data", dbPosts);

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

    public Observable<List<DbNotification>> getNotifications() {
        return Observable.defer(() -> Observable.just(
                mDaoSession.getDbNotificationDao().queryBuilder()
                        .orderDesc(DbNotificationDao.Properties.CreatedAt)
                        .list()
                )
        );
    }

    public Observable<Boolean> saveNotification(Notification pNotification){
        return Observable.defer(() -> Observable.just(
                mDaoSession.getDbNotificationDao()
                .insert(mDataMapper.transformNotificationForDb(pNotification)) != -1
            )
        );
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

    public Long updateFavouriteState(Long pId, Boolean isFavourite, boolean isSynced) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost post = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.eq(pId))
                .unique();
        if (isFavourite != null) {
            post.setIsFavourite(isFavourite);
            int count = (post.getFavouriteCount() != null) ? post.getFavouriteCount() : 0;
            post.setFavouriteCount(isFavourite
                    ? count + 1
                    : count - 1
            );
        }
        post.setIsSynced(isSynced);
        return postDao.insertOrReplace(post);
    }

    public Observable<List<DbCategory>> getCategoriesBySection(String section, boolean isCategory) {
        DbCategoryDao categoriesDao = mDaoSession.getDbCategoryDao();
        QueryBuilder queryBuilder = categoriesDao.queryBuilder();

        Join sectionJoin = queryBuilder.join(DbCategoryDao.Properties.SectionId, DbSection.class,
                DbSectionDao.Properties.Id);
        sectionJoin.where(DbSectionDao.Properties.Alias.eq(section));

        if (isCategory) {
            queryBuilder.whereOr(DbCategoryDao.Properties.ParentId.isNull(), DbCategoryDao
                    .Properties.ParentId.eq(0L));
        } else {
            queryBuilder.where(DbCategoryDao.Properties.ParentId.isNotNull());
            queryBuilder.where(DbCategoryDao.Properties.ParentId.notEq(0L));
        }

        List<DbCategory> categories = queryBuilder.orderAsc(DbCategoryDao.Properties.Position)
                .list();
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
