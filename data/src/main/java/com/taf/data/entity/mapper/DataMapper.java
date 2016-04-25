package com.taf.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taf.data.database.dao.DbCategory;
import com.taf.data.database.dao.DbPost;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostDataEntity;
import com.taf.data.entity.PostEntity;
import com.taf.data.entity.SyncDataEntity;
import com.taf.model.Category;
import com.taf.model.LatestContent;
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
            return post;
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
            return post;
        }
        return null;
    }

    public List<Category> transformCategory(List<DbCategory> pDbCategories) {
        List<Category> categories = new ArrayList<>();
        if (pDbCategories != null) {
            for (DbCategory dbCategory : pDbCategories) {
                Category category = transformCategory(dbCategory);
                if (category != null)
                    categories.add(category);
            }
        }
        return categories;
    }


    public Category transformCategory(DbCategory pDbCategories) {
        if (pDbCategories != null) {
            Category category = new Category();
            category.setId(pDbCategories.getId());
            category.setName(pDbCategories.getName());
            category.setIconUrl(pDbCategories.getIcon());
            category.setDetailImageUrl(pDbCategories.getDetailImage());
            category.setDetailIconUrl(pDbCategories.getDetailIcon());
            category.setParentId(pDbCategories.getParentId());
            category.setPosition(pDbCategories.getPosition());
            category.setCategoryId(pDbCategories.getCategoryId());
            category.setSectionName(pDbCategories.getSectionName());
            return category;
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
            return new SyncDataEntity(pSyncData.getId(), pSyncData.getStatus());
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
            return new SyncDataEntity(pPost.getId(), pPost.getIsFavourite()
                    ? SyncData.STATUS_LIKE
                    : SyncData.STATUS_DISLIKE);
        }
        return null;
    }
}
