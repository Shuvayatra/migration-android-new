package com.taf.shuvayatra.ui.fragment;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.FeedFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;

/**
 * Created by yipl on 1/11/17.
 */

public class NewsFragment extends FeedFragment {

    public static final String TAG = "NewsFragment";

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public String getToolbarTitle() {
        return getContext().getString(R.string.app_name);
    }

    @Override
    public int feedType() {
        return FEED_NEWS;
    }

    @Override
    public void initializeDaggerComponent() {
        DaggerDataComponent.builder().activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .dataModule(new DataModule())
                .build().inject(this);
        mPresenter.attachView(this);
    }
}
