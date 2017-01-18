package com.taf.data.utils;

import com.taf.model.Block;
import com.taf.model.Post;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by umesh on 1/18/17.
 */

public class Utils {

    public static List<Post> sortByPriorityPost(List<Post> posts) {
        if(posts != null) {
            Comparator<Post> comparator = new Comparator<Post>() {
                @Override
                public int compare(Post post, Post post2) {
                    return post2.getPriority() - post.getPriority();
                }
            };
            Collections.sort(posts, comparator);
        }
        return posts;
    }

    public static List<Block> sortByPositionBlock(List<Block> blocks) {
        if(blocks != null) {
            Comparator<Block> comparator = new Comparator<Block>() {
                @Override
                public int compare(Block block, Block block2) {
                    return block2.getPosition() - block.getPosition();
                }
            };
            Collections.sort(blocks, comparator);
        }
        return blocks;
    }
}
