package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PostDetailActivity;
import com.taf.shuvayatra.databinding.ArticleDetailDataBinding;

import butterknife.BindView;

public class ArticleDetailActivity extends PostDetailActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mContainer;

    @Override
    public int getLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void updateView(Post post) {
        ((ArticleDetailDataBinding) mBinding).setArticle(post);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContainer.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        loadPost();
    }

    @Override
    public void showLoadingView() {
        mContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mContainer.setRefreshing(false);
    }
}
