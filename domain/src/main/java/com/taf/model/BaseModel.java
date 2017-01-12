package com.taf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseModel implements Serializable {
    Long id;
    int mDataType;

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public int getDataType() {
        return mDataType;
    }

    public void setDataType(int pDataType) {
        mDataType = pDataType;
    }

//    public static List<BaseModel> getDummy() {
//        List<BaseModel> baseModels = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            List<Post> posts = new ArrayList<>();
//            Post post = new Post();
//            post.setTitle("title" + i);
//            post.setDescription("Description");
//            posts.add(post);
//            post.setType("audio");
//
//            PostData postData = new PostData();
//            post.setData(postData);
//
//            Block block = new Block();
//            block.setData(posts);
//            block.setLayout("list");
//            baseModels.add(block);
//        }
//        return baseModels;
//    }
}
