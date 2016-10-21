package com.taf.shuvayatra.ui.deprecated.interfaces;

import com.taf.shuvayatra.ui.views.LoadDataView;

public interface LatestContentView extends LoadDataView {
    void latestContentFetched(boolean hasNewContent);
}
