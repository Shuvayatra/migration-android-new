package com.taf.shuvayatra.ui.deprecated.interfaces;

import com.taf.shuvayatra.ui.views.LoadDataView;

import java.util.List;

public interface TagListView extends LoadDataView {
    void renderTagList(List<String> pTags);
}
