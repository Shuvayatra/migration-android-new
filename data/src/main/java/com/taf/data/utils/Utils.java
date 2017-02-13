package com.taf.data.utils;

import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.Post;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by umesh on 1/18/17.
 */

public class Utils {

    public static List<Post> sortByPriorityPost(List<Post> posts) {
        if (posts != null) {
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
        if (blocks != null) {
            Comparator<Block> comparator = (block, block2) -> block.getPosition() - block2.getPosition();
            Collections.sort(blocks, comparator);
        }
        return blocks;
    }

    public static List<Country> sortByCountryName(List<Country> countries) {
        Collections.sort(countries, (o1, o2) -> {
            if (o2.getTitleEnglish() == null && o1.getTitleEnglish() != null) {
                return -1;
            } else if (o1.getTitleEnglish() == null && o2.getTitleEnglish() != null) {
                return 1;
            } else if (o1.getTitleEnglish() == null && o2.getTitleEnglish() == null) {
                return 0;
            }
            return o1.getTitleEnglish().trim().toLowerCase().compareTo(o2.getTitleEnglish()
                    .trim().toLowerCase());
        });
        return countries;
    }
}
