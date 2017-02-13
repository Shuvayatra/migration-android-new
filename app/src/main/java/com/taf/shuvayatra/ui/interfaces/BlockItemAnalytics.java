package com.taf.shuvayatra.ui.interfaces;

import com.taf.model.Block;
import com.taf.model.Post;

/**
 * callback to track analytics
 */
public interface BlockItemAnalytics {

    public void onBlockItemSelected(Block block, Post post);
}
