package com.taf.shuvayatra.ui.views;

import com.taf.model.Block;
import com.taf.model.Post;
import com.taf.model.PostResponse;

import java.util.List;

/**
 * Created by yipl on 1/11/17.
 */

public interface NewsView extends LoadDataView{
    void renderBlocks(PostResponse data);
}
