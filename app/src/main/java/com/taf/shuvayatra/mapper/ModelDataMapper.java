package com.taf.shuvayatra.mapper;

import com.taf.shuvayatra.models.Block;
import com.taf.shuvayatra.models.Post;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by julian on 10/18/16.
 */

public class ModelDataMapper {

    @Inject
    public ModelDataMapper() {
    }

    public List<Block> transformBlock(List<com.taf.model.Block> models) {
        List<Block> blocks = new ArrayList<>();
        for (com.taf.model.Block model : models) {
            Block block = transformBlock(model);
            if (block != null) blocks.add(block);
        }
        return blocks;
    }

    private Block transformBlock(com.taf.model.Block model) {
        if (model != null) {
            Block block = new Block();
            block.setLayout(model.getLayout());
            block.setTitle(model.getTitle());
            block.setData(transformPost(model.getData()));
            return block;
        }
        return null;
    }

    public List<Post> transformPost(List<com.taf.model.Post> models) {
        List<Post> blocks = new ArrayList<>();
        for (com.taf.model.Post model : models) {
            Post post = transformPost(model);
            if (post != null) blocks.add(post);
        }
        return blocks;
    }

    private Post transformPost(com.taf.model.Post model) {
        if (model != null) {
            Post post = new Post();
            post.setTitle(model.getTitle());
            post.setDescription(model.getDescription());
            return post;
        }
        return null;
    }
}
