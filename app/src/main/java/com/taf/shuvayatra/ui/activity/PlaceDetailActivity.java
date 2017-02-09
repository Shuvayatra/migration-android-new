package com.taf.shuvayatra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.PostDetailActivity;
import com.taf.shuvayatra.databinding.ArticleDetailDataBinding;
import com.taf.shuvayatra.databinding.PlaceDetailDataBinding;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import butterknife.BindView;

/**
 * Created by umesh on 2/8/17.
 */

public class PlaceDetailActivity extends PostDetailActivity implements
        SwipeRefreshLayout.OnRefreshListener, ListItemClickListener,
        AppBarLayout.OnOffsetChangedListener  {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mContainer;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.similar_story_list)
    EmptyStateRecyclerView mEmptyStateRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer.setOnRefreshListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        mEmptyStateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEmptyStateRecyclerView.setAdapter(new ListAdapter(this, this));
        mEmptyStateRecyclerView.setEmptyView(mEmptyView);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (appBarLayout.getTotalScrollRange() == Math.abs(verticalOffset)) {
            if (mPost != null) {
                collapsingToolbarLayout.setTitle(mPost.getTitle());
            }
        } else {
            if (collapsingToolbarLayout.getTitle() != null) {
                collapsingToolbarLayout.setTitle(null);
            }
        }
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

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {

        Intent intent = null;

        if (((Post) pModel).getType().equalsIgnoreCase(Post.TYPE_AUDIO)) {
            intent = new Intent(this, AudioDetailActivity.class);
        } else if (((Post) pModel).getType().equalsIgnoreCase(Post.TYPE_NEWS) ||
                ((Post) pModel).getType().equalsIgnoreCase(Post.TYPE_TEXT)) {
            intent = new Intent(this, ArticleDetailActivity.class);
        } else if (((Post) pModel).getType().equalsIgnoreCase(Post.TYPE_VIDEO)) {
            intent = new Intent(this, VideoDetailActivity.class);
        }

        if (intent != null) {
            intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
            startActivity(intent);
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_places_detail;
    }

    @Override
    protected void updateView(Post post) {

        ((PlaceDetailDataBinding) mBinding).setPlace(post);
        ((PlaceDetailDataBinding) mBinding).setListener(this);
        ((PlaceDetailDataBinding) mBinding).setSimilarStories(post.getSimilarPosts());
    }
}
