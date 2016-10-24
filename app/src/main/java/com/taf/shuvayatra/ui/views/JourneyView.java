package com.taf.shuvayatra.ui.views;

import com.taf.model.Block;
import com.taf.shuvayatra.ui.deprecated.interfaces.LoadDataView;

import java.util.List;

public interface JourneyView extends LoadDataView{
    void renderContents(List<Block> journeyContents);
}
