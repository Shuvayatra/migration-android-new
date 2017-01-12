package com.taf.shuvayatra.ui.views;

import com.taf.model.Block;

import java.util.List;

/**
 * Created by yipl on 1/11/17.
 */

public interface NewsView extends LoadDataView{
    void renderBlocks(List<Block> data);
}
