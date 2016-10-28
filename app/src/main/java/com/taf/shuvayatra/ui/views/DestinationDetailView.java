package com.taf.shuvayatra.ui.views;

import com.taf.model.Block;

import java.util.List;

public interface DestinationDetailView extends LoadDataView {
    void renderBlocks(List<Block> blocks);
}
