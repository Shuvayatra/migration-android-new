package com.taf.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taf.data.database.dao.DbCategory;
import com.taf.data.database.dao.DbNotification;
import com.taf.data.database.dao.DbPost;
import com.taf.data.database.dao.DbTag;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.CategoryEntity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostDataEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.model.Category;
import com.taf.model.LatestContent;
import com.taf.model.Notification;
import com.taf.model.Post;
import com.taf.model.PostData;
import com.taf.model.SyncData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@PerActivity
public class DataMapper {

    @Inject
    public DataMapper() {
    }

    public LatestContent transformLatestContent(LatestContentEntity pEntity) {
        LatestContent latestContent = null;
        if (pEntity != null) {
            latestContent = new LatestContent();
            latestContent.setPosts(transformPost(pEntity.getPosts()));
        }
        return latestContent;
    }

    public List<Post> transformPost(List<PostEntity> pEntities) {
        List<Post> postList = new ArrayList<>();
        if (pEntities != null) {
            for (PostEntity entity : pEntities) {
                Post post = transformPost(entity);
                if (post != null)
                    postList.add(post);
            }
        }
        return postList;
    }

    public Post transformPost(PostEntity pEntity) {
        if (pEntity != null) {
            Post post = new Post();
            post.setId(pEntity.getId());
            post.setType(pEntity.getType());
            post.setTitle(pEntity.getTitle());
            post.setDescription(pEntity.getDescription());
            post.setSource(pEntity.getSource());
            post.setUpdatedAt(pEntity.getUpdatedAt());
            post.setCreatedAt(pEntity.getCreatedAt());
            post.setTags(pEntity.getTags());
            post.setData(transformPostData(pEntity.getData()));
            post.setFeaturedImage(pEntity.getFeaturedImage());
            post.setShare(pEntity.getShareCount());
            post.setLikes(pEntity.getFavouriteCount());
            post.setViewCount(pEntity.getViewCount());
            return post;
        }
        return null;
    }

    public PostData transformPostData(PostDataEntity pEntity) {
        PostData data = null;
        if (pEntity != null) {
            data = new PostData();
            data.setContent(pEntity.getContent());
            data.setMediaUrl(pEntity.getMediaUrl());
            data.setDuration(pEntity.getDuration());
            data.setThumbnail(pEntity.getThumbnail());
            data.setPhoneNumbers(pEntity.getPhoneNumbers());
            data.setAddress(pEntity.getAddress());
        }
        return data;
    }

    public DbPost transformPostForDB(PostEntity pEntity) {
        if (pEntity != null) {
            Gson gson = new Gson();
            DbPost post = new DbPost(pEntity.getId());
            post.setTitle(pEntity.getTitle());
            post.setDescription(pEntity.getDescription());
            post.setType(pEntity.getType());
            post.setData(gson.toJson(pEntity.getData()));
            post.setTags(gson.toJson(pEntity.getTags()));
            post.setType(pEntity.getType());
            post.setUpdatedAt(pEntity.getUpdatedAt());
            post.setCreatedAt(pEntity.getCreatedAt());
            post.setFavouriteCount(pEntity.getFavouriteCount());
            post.setFeaturedImage(pEntity.getFeaturedImage());
            post.setShareCount(pEntity.getShareCount());
            post.setViewCount(pEntity.getViewCount());
            return post;
        }
        return null;
    }

    public DbCategory transformCategoryForDB(CategoryEntity pEntity) {
        if (pEntity != null) {
            DbCategory category = new DbCategory(pEntity.getId());
            category.setParentId(pEntity.getParentId() == null ? 0 : pEntity.getParentId());
            category.setTitle(pEntity.getTitle());
            category.setCoverImageUrl(pEntity.getCoverImageUrl());
            category.setIconUrl(pEntity.getIconUrl());
            category.setSmallIconUrl(pEntity.getSmallIconUrl());
            category.setPosition(pEntity.getPosition());
            category.setCreatedAt(pEntity.getCreatedAt());
            category.setUpdatedAt(pEntity.getUpdatedAt());
            category.setLeftIndex(pEntity.getLeftIndex());
            category.setRightIndex(pEntity.getRightIndex());
            category.setDepth(pEntity.getDepth());
            category.setAlias(pEntity.getAlias());
            category.setParentAlias(pEntity.getParentAlias());
            return category;
        }
        return null;
    }

    public List<Post> transformPostFromDb(Map<String, Object> pObjectMap) {
        List<Post> postList = new ArrayList<>();
        int totalCount = (int) pObjectMap.get("total_count");
        List<DbPost> data = ((List<DbPost>) pObjectMap.get("data"));
        if (data != null) {
            for (DbPost dbPost : data) {
                Post post = transformPostFromDb(dbPost, totalCount);
                if (post != null) {
                    postList.add(post);
                }
            }
        }
        return postList;
    }

    public List<Post> transformPostFromDb(List<DbPost> pData) {
        List<Post> postList = new ArrayList<>();
        for (DbPost dbPost : pData) {
            Post post = transformPostFromDb(dbPost, 0);
            if (post != null) {
                postList.add(post);
            }
        }
        return postList;
    }

    public Post transformPostFromDb(DbPost pPost, int totalCount) {
        if (pPost != null) {
            Gson gson = new Gson();
            Post post = new Post();
            post.setId(pPost.getId());
            post.setTitle(pPost.getTitle());
            post.setDescription(pPost.getDescription());
            post.setType(pPost.getType());
            post.setData(transformPostData(gson.fromJson(pPost.getData(), PostDataEntity.class)));
            post.setTags(gson.fromJson(pPost.getTags(), new TypeToken<List<String>>() {
            }.getType()));
            post.setType(pPost.getType());
            post.setUpdatedAt(pPost.getUpdatedAt());
            post.setCreatedAt(pPost.getCreatedAt());
            post.setIsFavourite(pPost.getIsFavourite());
            post.setIsSynced(pPost.getIsSynced());
            post.setTotalCount(totalCount);
            post.setCategory(pPost.getCategory());
            post.setViewCount(pPost.getViewCount());
            post.setUnSyncedViewCount(pPost.getUnsyncedViewCount());
            post.setFeaturedImage(pPost.getFeaturedImage());
            post.setLikes(pPost.getFavouriteCount());
            post.setShare(pPost.getShareCount());
            post.setUnSyncedShareCount(pPost.getUnsyncedShareCount());
            return post;
        }
        return null;
    }

    public List<Category> transformCategoryFromDb(List<DbCategory> pDbCategories) {
        List<Category> categories = new ArrayList<>();
        if (pDbCategories != null) {
            for (DbCategory dbCategory : pDbCategories) {
                Category category = transformCategoryFromDb(dbCategory);
                if (category != null)
                    categories.add(category);
            }
        }
        return categories;
    }


    public Category transformCategoryFromDb(DbCategory pDbCategory) {
        if (pDbCategory != null) {
            Category category = new Category();
            category.setId(pDbCategory.getId());
            category.setTitle(pDbCategory.getTitle());
            category.setIconUrl(pDbCategory.getIconUrl());
            category.setCoverImageUrl(pDbCategory.getCoverImageUrl());
            category.setSmallIconUrl(pDbCategory.getSmallIconUrl());
            category.setParentId(pDbCategory.getParentId());
            category.setPosition(pDbCategory.getPosition());
            category.setAlias(pDbCategory.getAlias());
            category.setParentAlias(pDbCategory.getParentAlias());
            return category;
        }
        return null;
    }

    public List<Notification> transformNotificationFromDb(List<DbNotification> pData) {
        List<Notification> notificationList = new ArrayList<>();
        if (pData != null) {
            for (DbNotification dbNotification : pData) {
                Notification notification = transformNotificationFromDb(dbNotification);
                if (notification != null) {
                    notificationList.add(notification);
                }
            }
        }
        return notificationList;
    }

    public Notification transformNotificationFromDb(DbNotification pData) {
        if (pData != null) {
            Notification notification = new Notification();
            notification.setId(pData.getId());
            notification.setTitle(pData.getTitle());
            notification.setDescription(pData.getDescription());
            notification.setCreatedAt(pData.getCreatedAt());
            notification.setUpdatedAt(pData.getUpdatedAt());
            return notification;
        }
        return null;
    }

    public DbNotification transformNotificationForDb(Notification pData) {
        if (pData != null) {
            DbNotification notification = new DbNotification();
            notification.setId(pData.getId());
            notification.setTitle(pData.getTitle());
            notification.setDescription(pData.getDescription());
            notification.setCreatedAt(pData.getCreatedAt());
            notification.setUpdatedAt(pData.getUpdatedAt());
            return notification;
        }
        return null;
    }

    public List<SyncDataEntity> transformSyncData(List<SyncData> pDataList) {
        List<SyncDataEntity> list = new ArrayList<>();
        if (pDataList != null) {
            for (SyncData data : pDataList) {
                SyncDataEntity entity = transformSyncData(data);
                if (entity != null) {
                    list.add(entity);
                }
            }
        }
        return list;
    }

    public SyncDataEntity transformSyncData(SyncData pSyncData) {
        if (pSyncData != null) {
            return new SyncDataEntity(pSyncData.getId(), pSyncData.getLike(), pSyncData.getViewCount(), pSyncData.getShareCount());
        }
        return null;
    }

    public List<SyncDataEntity> transformPostForSync(List<DbPost> pDataList) {
        List<SyncDataEntity> list = new ArrayList<>();
        if (pDataList != null) {
            for (DbPost data : pDataList) {
                SyncDataEntity entity = transformPostForSync(data);
                if (entity != null) {
                    list.add(entity);
                }
            }
        }
        return list;
    }

    public SyncDataEntity transformPostForSync(DbPost pPost) {
        if (pPost != null) {
            return new SyncDataEntity(pPost.getId(), pPost.getIsFavourite() == null ? null : pPost.getIsFavourite(),
                    pPost.getUnsyncedViewCount(), pPost.getUnsyncedShareCount());
        }
        return null;
    }

    public List<String> transformTags(List<DbTag> data) {
        List<String> tags = new ArrayList<>();
        for (DbTag dbTag : data) {
            if (dbTag != null && dbTag.getTitle() != null && !dbTag.getTitle().isEmpty()) {
                tags.add(dbTag.getTitle());
            }
        }
        return tags;
    }
}
