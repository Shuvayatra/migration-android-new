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
import com.taf.data.database.dao.DbTag;
import com.taf.data.database.dao.DbTagDao;
import com.taf.data.entity.CategoryEntity;
import com.taf.data.entity.DeletedContentDataEntity;
import com.taf.data.entity.DeletedContentEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.utils.Logger;
import com.taf.model.Notification;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import de.greenrobot.dao.query.Join;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
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

    public void clearCache() {
        mDaoSession.clear();
    }

    public void insertUpdate(LatestContentEntity pEntity) {
        insertUpdateCategories(pEntity.getCategories());
        insertUpdatePosts(pEntity.getPosts());
    }

    public void insertUpdatePosts(List<PostEntity> pEntities) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPostCategoryDao postCategoryDao = mDaoSession.getDbPostCategoryDao();
        for (PostEntity entity : pEntities) {
            DbPost post = mDataMapper.transformPostForDB(entity);
            if (post != null) {
                Long insertId = postDao.insertOrReplace(post);
                deleteAllPostCategoryRelations(insertId);
                for (Long catId : entity.getCategoryIds()) {
                    DbPostCategory item = new DbPostCategory();
                    item.setCategoryId(catId);
                    item.setPostId(insertId);
                    postCategoryDao.insertOrReplace(item);
                }
                insertTags(entity.getTags());
            }
        }
    }

    public void deleteAllPostCategoryRelations(Long postId) {
        DbPostCategoryDao postCategoryDao = mDaoSession.getDbPostCategoryDao();
        postCategoryDao.queryBuilder()
                .where(DbPostCategoryDao.Properties.PostId.eq(postId))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void insertUpdateCategories(List<CategoryEntity> pEntities) {
        DbCategoryDao categoryDao = mDaoSession.getDbCategoryDao();
        for (CategoryEntity entity : pEntities) {
            DbCategory category = mDataMapper.transformCategoryForDB(entity);
            if (category != null) {
                categoryDao.insertOrReplace(category);
            }
        }
    }

    public void insertTags(List<String> tags) {
        if (tags != null) {
            DbTagDao tagDao = mDaoSession.getDbTagDao();
            for (String tag : tags) {
                if (tag != null) {
                    long count = tagDao.queryBuilder()
                            .where(DbTagDao.Properties.Title.eq(tag))
                            .count();
                    if (count < 1) {
                        DbTag dbTag = new DbTag();
                        dbTag.setTitle(tag);
                        tagDao.insert(dbTag);
                    }
                }
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

    public Observable<Map<String, Object>> getSimilarPosts(int pLimit, int pOffset, @NonNull
    String pType, List<String> pTags, List<Long> pExcludeIds) {
        if (pTags == null || pTags.isEmpty()) {
            return Observable.empty();
        }
        //// TODO: 5/13/16 better implementation
        if (pTags.isEmpty()) {
            pTags.add("dummy-text");
        }
        return getPosts(pLimit, pOffset, pType, false, null, null, pTags, pExcludeIds, null);
    }

    public Observable<Map<String, Object>> getPostByCategory(Long categoryId, int pLimit, int
            pOffset, String pType, List<String> excludeTypes, List<Long> excludeIds) {
        return getPosts(pLimit, pOffset, pType, false, categoryId, null, null, excludeIds, excludeTypes);
    }

    public Observable<Map<String, Object>> getPostWithExcludes(int pLimit, int pOffset,
                                                               List<String> excludeTypes) {
        return getPosts(pLimit, pOffset, null, false, null, null, null, null, excludeTypes);
    }

    public Observable<Map<String, Object>> getPosts(int pLimit, int pOffset, String pType, boolean
            pFavouritesOnly) {
        return getPosts(pLimit, pOffset, pType, pFavouritesOnly, null, null, null, null, null);
    }

    public Observable<Map<String, Object>> getPostsByTitle(int pLimit, int pOffset, String title) {
        Logger.e("DatabaseHelper", "title: " + title);
        return getPosts(pLimit, pOffset, null, false, null, title, null, null, null);
    }

    public Observable<Map<String, Object>> getPostByTags(int pLimit, int pOffset, List<String> pTags) {
        return getPosts(pLimit, pOffset, null, false, null, null, pTags, null, null);
    }

    public Observable<Map<String, Object>> getPosts(int pLimit, int pOffset, String pType, boolean
            pFavouritesOnly, Long pCategoryId, String pTitle, List<String> pTags, List<Long> pExcludeIds, List<String> pExcludeTypes) {
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
        if (pExcludeTypes != null) {
            queryBuilder.where(DbPostDao.Properties.Type.notIn(pExcludeTypes));
        }
        if (pTags != null) {
            String whereClause = "(";
            for (int i = 0; i < pTags.size(); i++) {
                if (i != 0) {
                    whereClause += " or ";
                }
                whereClause += "tags like '%" + pTags.get(i) + "%'";
            }
            whereClause += ")";
            queryBuilder.where(new WhereCondition.StringCondition(whereClause));
        }

        if (pTitle != null) {
//            queryBuilder.where(DbPostDao.Properties.Title.like(pTitle));
            queryBuilder.where(new WhereCondition.StringCondition("T.title like '%" + pTitle + "%'"));
        }

        Join postJoin = queryBuilder.join(DbPostDao.Properties.Id, DbPostCategory.class,
                DbPostCategoryDao.Properties.PostId);
        Join categoryJoin = queryBuilder.join(postJoin, DbPostCategoryDao.Properties.CategoryId,
                DbCategory.class, DbCategoryDao.Properties.Id);
        if (pCategoryId != null) {
            categoryJoin.whereOr(DbCategoryDao.Properties.Id.eq(pCategoryId), DbCategoryDao.Properties.ParentId.eq(pCategoryId));
        }

        List<DbPost> dbPosts = queryBuilder
                .limit(pLimit)
                .offset(pOffset)
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .distinct()
                .list();
        Logger.e("DatabaseHelper", "dbpost: "+ dbPosts);
        for (DbPost dbPost : dbPosts) {
            QueryBuilder<DbCategory> queryBuilder1 = mDaoSession.getDbCategoryDao().queryBuilder();
            queryBuilder1.join(DbCategoryDao.Properties.Id, DbPostCategory.class,
                    DbPostCategoryDao.Properties.CategoryId)
                    .where(DbPostCategoryDao.Properties.PostId.eq(dbPost.getId()));
            dbPost.setCategoryList(
                    queryBuilder1.orderAsc(DbCategoryDao.Properties.Position)
                            .list()
            );
            Logger.e("DatabaseHelper", "post: " + dbPost.getCategoryList());
        }

        map.put("total_count", queryBuilder.list().size());
        map.put("data", dbPosts);

        return Observable.defer(() -> Observable.just(map));
    }

    public Observable<Map<String, Object>> getPostsNew(int pLimit, int pOffset, String pType,
                                                       boolean pFavouritesOnly, Long pCategoryId,
                                                       Long pSubCategoryId, List<String> pTags,
                                                       String pTitle, List<Long>
                                                               pExcludeIds, List<String>
                                                               pExcludeTypes) {
        Map<String, Object> map = new HashMap<>();
        DbPostDao postDao = mDaoSession.getDbPostDao();

        QueryBuilder<DbPost> queryBuilder = postDao.queryBuilder();
        if (pType != null) {
            queryBuilder.where(DbPostDao.Properties.Type.eq(pType));
        }
        if (pTitle != null) {
            queryBuilder.where(DbPostDao.Properties.Title.like("%" + pTitle + "%"));
        }
        if (pFavouritesOnly) {
            queryBuilder.where(DbPostDao.Properties.IsFavourite.eq(true));
        }
        if (pExcludeIds != null) {
            queryBuilder.where(DbPostDao.Properties.Id.notIn(pExcludeIds));
        }
        if (pExcludeTypes != null) {
            queryBuilder.where(DbPostDao.Properties.Type.notIn(pExcludeTypes));
        }
        if (pTags != null && !pTags.isEmpty()) {
            String whereClause = "(";
            for (int i = 0; i < pTags.size(); i++) {
                if (i != 0) {
                    whereClause += " or ";
                }
                whereClause += "tags like '%" + pTags.get(i) + "%'";
            }
            whereClause += ")";
            queryBuilder.where(new WhereCondition.StringCondition(whereClause));
        }

        Join postJoin = queryBuilder.join(DbPostDao.Properties.Id, DbPostCategory.class,
                DbPostCategoryDao.Properties.PostId);
        Join categoryJoin = queryBuilder.join(postJoin, DbPostCategoryDao.Properties.CategoryId,
                DbCategory.class, DbCategoryDao.Properties.Id);
        if (pCategoryId != null) {
            categoryJoin.whereOr(DbCategoryDao.Properties.Id.eq(pCategoryId), DbCategoryDao
                    .Properties.ParentId.eq(pCategoryId));
        }
        Logger.e("DatabaseHelper", " sub category Id"+ pSubCategoryId);
        if (pSubCategoryId != null) {
            categoryJoin.where(DbCategoryDao.Properties.Id.eq(pSubCategoryId));
        }

        QueryBuilder queryBuilderSub = queryBuilder;
        map.put("total_count", queryBuilder.distinct().list().size());

        List<DbPost> dbPosts = queryBuilderSub
                .limit(pLimit)
                .offset(pOffset)
                .orderDesc(DbPostDao.Properties.CreatedAt)
                .distinct()
                .list();
        Logger.d("DatabaseHelper_getPostsNew", "list :" + pLimit + "/" + pOffset);
        Logger.d("DatabaseHelper_getPostsNew", "listSize: " + dbPosts.size());

        for (DbPost dbPost : dbPosts) {
            QueryBuilder<DbCategory> queryBuilder1 = mDaoSession.getDbCategoryDao().queryBuilder();
            queryBuilder1.join(DbCategoryDao.Properties.Id, DbPostCategory.class,
                    DbPostCategoryDao.Properties.CategoryId)
                    .where(DbPostCategoryDao.Properties.PostId.eq(dbPost.getId()));
            List<DbCategory> dbCategories =  queryBuilder1.orderAsc(DbCategoryDao.Properties.Depth,DbCategoryDao.Properties.Position)
                    .list();
            for (DbCategory dbCategory : dbCategories) {
                    queryBuilder1 = mDaoSession.getDbCategoryDao().queryBuilder();
                DbCategory parent = queryBuilder1.where(DbCategoryDao.Properties.Id.eq(dbCategory.getParentId())).unique();
                if(parent.getAlias()!=null) {
                    dbCategory.setParentAlias(parent.getAlias());
                    Logger.e("DatabaseHelper", "not null parent alias: "+ dbCategory.getParentAlias()+" title: \t"+ dbCategory.getTitle());

                }else{
                    dbCategory.setAlias(parent.getTitle());
                    Logger.e("DatabaseHelper", "null parent alias: "+ parent.getAlias());

                }

//                List<DbCategory> parents = queryBuilder1.where(DbCategoryDao.Properties.LeftIndex.lt(dbCategory.getLeftIndex()),
//                        DbCategoryDao.Properties.Depth.lt(dbCategory.getDepth())).list();

            }
            dbPost.setCategoryList(dbCategories);
        }
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

    public Observable<Boolean> saveNotification(Notification pNotification) {
        return Observable.defer(() -> Observable.just(
                mDaoSession.getDbNotificationDao()
                        .insert(mDataMapper.transformNotificationForDb(pNotification)) != -1
                )
        );
    }

    public void updateFavouriteState(List<Long> pIds, boolean isSynced) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        List<DbPost> postList = postDao.queryBuilder()
                .where(DbPostDao.Properties.Id.in(pIds))
                .list();
        for (DbPost dbPost : postList) {
            dbPost.setIsSynced(isSynced);
            if (isSynced)
                dbPost.setUnsyncedViewCount(0);
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

    public Observable<List<DbCategory>> getCategoriesBySection(String section, Boolean isCategory,
                                                               Long pParentId) {
        DbCategoryDao categoriesDao = mDaoSession.getDbCategoryDao();
        QueryBuilder queryBuilder = categoriesDao.queryBuilder();
        Map<Long, String> aliasMapping = null;
        DbCategory parentCategory = null;
        if (section != null && !section.equals(MyConstants.SECTION.INFO)) {
            parentCategory = categoriesDao.queryBuilder().where(DbCategoryDao.Properties.Alias.eq
                    (section)).unique();
        } else {
            List<DbCategory> parentList = categoriesDao.queryBuilder().where(DbCategoryDao.Properties.Depth.eq(0)).list();
            aliasMapping = new HashMap<>();
            for (DbCategory category : parentList) {
                aliasMapping.put(category.getId(), category.getTitle());
            }
        }

        int parentDepth = 0;
        if (parentCategory != null) {
            queryBuilder.where(DbCategoryDao.Properties.LeftIndex.between(parentCategory.getLeftIndex
                    (), parentCategory.getRightIndex()));
            parentDepth = parentCategory.getDepth();
        }

        if (isCategory == null) {
        } else if (isCategory) {
            queryBuilder.where(DbCategoryDao.Properties.Depth.eq(parentDepth + 1));
        } else {
            queryBuilder.where(DbCategoryDao.Properties.Depth.eq(parentDepth + 2));
        }

        if (pParentId != null && pParentId != Long.MIN_VALUE) {
            queryBuilder.where(DbCategoryDao.Properties.ParentId.eq(pParentId));
        }

        List<DbCategory> categories = queryBuilder.orderAsc(DbCategoryDao.Properties.ParentId, DbCategoryDao.Properties.Position)
                .list();
        for (DbCategory category : categories) {
            if (section.equals(MyConstants.SECTION.INFO)) {
                category.setParentAlias(aliasMapping.get(category.getParentId()));
            } else {
                category.setParentAlias(parentCategory != null ? parentCategory.getAlias()
                        : "");
            }
        }
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

    public long updateUnSyncedViewCount(Long pId) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost post = postDao.queryBuilder().where(DbPostDao.Properties.Id.eq(pId)).unique();
        post.setUnsyncedViewCount((post.getUnsyncedViewCount() != null ? post.getUnsyncedViewCount() : 0) + 1);
        post.setIsSynced(false);
        return postDao.insertOrReplace(post);
    }

    public long updateUnSyncedShareCount(Long pId) {
        DbPostDao postDao = mDaoSession.getDbPostDao();
        DbPost post = postDao.queryBuilder().where(DbPostDao.Properties.Id.eq(pId)).unique();
        post.setUnsyncedShareCount((post.getUnsyncedShareCount() != null ? post.getUnsyncedShareCount() : 0) + 1);
        post.setIsSynced(false);
        return postDao.insertOrReplace(post);
    }

    public Observable<List<DbTag>> getTags() {
        List<DbTag> tags = mDaoSession.getDbTagDao()
                .queryBuilder().list();

        return Observable.defer(() -> Observable.just(tags));
    }

    public void deleteContents(DeletedContentDataEntity pContent) {
        List<Long> deleteIds = new ArrayList<>();
        if (pContent.getPosts() != null) {
            for (DeletedContentEntity post : pContent.getPosts()) {
                deleteIds.add(post.getId());
            }
            mDaoSession.getDbPostDao().queryBuilder()
                    .where(DbPostDao.Properties.Id.in(deleteIds))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            mDaoSession.getDbPostCategoryDao().queryBuilder()
                    .where(DbPostCategoryDao.Properties.PostId.in(deleteIds))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
        }

        if (pContent.getSections() != null) {
            deleteIds.clear();
            for (DeletedContentEntity section : pContent.getSections()) {
                deleteIds.add(section.getId());
            }
            mDaoSession.getDbCategoryDao().queryBuilder()
                    .where(DbCategoryDao.Properties.Id.in(deleteIds))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            mDaoSession.getDbPostCategoryDao().queryBuilder()
                    .where(DbPostCategoryDao.Properties.CategoryId.in(deleteIds))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
        }
    }
}
