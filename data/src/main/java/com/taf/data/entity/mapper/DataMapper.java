package com.taf.data.entity.mapper;

import com.google.gson.Gson;
import com.taf.data.database.dao.dbPost;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.LatestContentEntity;
import com.taf.data.entity.PostEntity;
import com.taf.model.LatestContent;
import com.taf.model.Post;

import java.util.ArrayList;
import java.util.List;

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
            post.setUpdatedAt(pEntity.getUpdatedAt());
            post.setCreatedAt(pEntity.getCreatedAt());
            post.setTags(pEntity.getTags());
            return post;
        }
        return null;
    }

    public dbPost transformPostForDB(PostEntity pEntity) {
        if (pEntity != null) {
            dbPost post = new dbPost(pEntity.getId());
            post.setUpdatedAt(pEntity.getUpdatedAt());
            post.setCreatedAt(pEntity.getCreatedAt());
            post.setDescription(pEntity.getDescription());
            post.setTitle(pEntity.getTitle());
            post.setTags(new Gson().toJson(pEntity.getTags()));
            post.setType(pEntity.getType());
        }
        return null;
    }
}
