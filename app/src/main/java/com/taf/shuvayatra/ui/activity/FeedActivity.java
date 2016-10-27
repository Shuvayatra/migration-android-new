package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.model.PostResponse;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PostListPresenter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.deprecated.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.deprecated.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.deprecated.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.PostListView;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import butterknife.BindView;

public class FeedActivity extends BaseActivity implements
        ListItemClickListener,
        PostListView,
        SwipeRefreshLayout.OnRefreshListener {

    public static final Integer PAGE_LIMIT = 15;
    public static final Integer INITIAL_OFFSET = 1;
    private static final int REQUEST_CODE_POST_DETAIL = 3209;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    @BindView(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;

    @Inject
    PostListPresenter mPresenter;

    LinearLayoutManager mLayoutManager;
    ListAdapter<Post> mListAdapter;
    UseCaseData mUseCaseData = new UseCaseData();
    Integer mPage = INITIAL_OFFSET;
    Integer mTotalDataCount = 0;
    int listItemSelection;
    boolean mIsLoading = false;
    boolean mIsLastPage = false;

    @Override
    public int getLayout() {
        return R.layout.activity_feed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getStringExtra("title");
        if (title != null) getSupportActionBar().setTitle(title);

        Uri data = getIntent().getData();
        String params = null;
        if (data != null) {
            params = data.getQueryParameter("category_id");
        }

        initialize(params);
        setUpAdapter();

        loadPostsList(INITIAL_OFFSET);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize(String params) {
        DaggerDataComponent.builder()
                .dataModule(new DataModule(params))
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    private void setUpAdapter() {
        mListAdapter = new ListAdapter(getContext(), this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeContainer.setOnRefreshListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (!mIsLoading && !mIsLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadPostsList(mPage + 1);
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    private void loadPostsList(Integer pPage) {
        mPage = pPage;
        mSwipeContainer.setRefreshing(true);
        mUseCaseData.clearAll();
        mUseCaseData.putInteger(UseCaseData.OFFSET, pPage);
        mUseCaseData.putInteger(UseCaseData.LIMIT, PAGE_LIMIT);

        mPresenter.initialize(mUseCaseData);
    }

    @Override
    public void onRefresh() {
        loadPostsList(INITIAL_OFFSET);
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        Intent intent = null;
        listItemSelection = pIndex;
        switch (pModel.getDataType()) {
            case MyConstants.Adapter.TYPE_VIDEO:
                intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_VIDEO, pModel);
                break;
            case MyConstants.Adapter.TYPE_TEXT:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ARTICLE, pModel);
                break;
            case MyConstants.Adapter.TYPE_AUDIO:
                intent = new Intent(getContext(), AudioDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_AUDIO, pModel);
                break;
        }
        if (intent != null)
            startActivityForResult(intent, REQUEST_CODE_POST_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_POST_DETAIL) {
                boolean status = data.getBooleanExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS,
                        false);
                int viewCount = data.getIntExtra(MyConstants.Extras.KEY_VIEW_COUNT, 0);
                if (viewCount != 0) {
                    mListAdapter.getDataCollection().get(listItemSelection).setUnSyncedViewCount(viewCount);
                }
                int shareCount = data.getIntExtra(MyConstants.Extras.KEY_SHARE_COUNT, 0);
                if (shareCount != 0) {
                    mListAdapter.getDataCollection().get(listItemSelection).setUnSyncedShareCount(shareCount);
                }
                int favCount = data.getIntExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, 0);
                mListAdapter.getDataCollection().get(listItemSelection).setLikes(favCount);
                mListAdapter.getDataCollection().get(listItemSelection).setIsFavourite(status);
                mListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void renderPostList(PostResponse response) {
        if (response.isFromCache()) {
            if (mListAdapter.getItemCount() == 0) {
                mListAdapter.setDataCollection(response.getData());
                mIsLastPage = true;
            }
            return;
        }

        Logger.d("FeedActivity_renderPostList", "pagination: " + mListAdapter.getItemCount());
        if (mPage == INITIAL_OFFSET) {
            mListAdapter.setDataCollection(response.getData());
        } else {
            mListAdapter.addDataCollection(response.getData());
        }
        mTotalDataCount = response.getTotal();
        mIsLastPage = (mPage == response.getLastPage());
        mPage = response.getCurrentPage();
        Logger.d("FeedActivity_renderPostList", "pagination: " + mListAdapter.getItemCount());
    }

    @Override
    public void showLoadingView() {
        Logger.d("FeedActivity_showLoadingView", "pagination start");
        mIsLoading = true;
        mSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        Logger.d("FeedActivity_hideLoadingView", "pagination end");
        mIsLoading = false;
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
