package com.taf.shuvayatra.ui.views;

import com.taf.model.Block;
import com.taf.shuvayatra.ui.deprecated.interfaces.LoadDataView;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public interface HomeView extends LoadDataView {
    void renderBlocks(List<Block> data);
}
