package com.taf.shuvayatra.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.taf.data.entity.FeedType;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.model.PostResponse;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.presenter.PostListPresenter;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.FeedActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.PostListView;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import butterknife.BindView;

/**
 *
 */
@Deprecated
public abstract class FeedFragment extends BaseFragment implements
        ListItemClickListener,
        PostListView,
        SwipeRefreshLayout.OnRefreshListener {

    public static final Integer PAGE_LIMIT = 15;
    public static final Integer INITIAL_OFFSET = 1;

    /**
     * standard ids for feed fragment
     */
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;

    LinearLayoutManager mLayoutManager;
    ListAdapter<Post> mListAdapter;
    UseCaseData mUseCaseData = new UseCaseData();
    Integer mPage = INITIAL_OFFSET;
    Integer mTotalDataCount = 0;
    int listItemSelection;
    boolean mIsLoading = false;
    boolean mIsLastPage = false;

    public abstract String getToolbarTitle();

    public static final int FEED_GENERAL = 0;
    public static final int FEED_NEWS = 1;

    /**
     * feed type
     * {@link #FEED_NEWS}, or
     * {@link #FEED_GENERAL}
     */
    @FeedType
    public abstract int feedType();

    @Inject
    public PostListPresenter mPresenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (((BaseActivity) getActivity()).getSupportActionBar() != null)
            ((BaseActivity) getActivity()).getSupportActionBar().setTitle(getToolbarTitle());

        initializeDaggerComponent();
        setupAdapter();
        loadPostsList(INITIAL_OFFSET);
    }

    public abstract void initializeDaggerComponent();

    public void setupAdapter() {
        mListAdapter = new ListAdapter(getContext(), this);
        mLayoutManager = new LinearLayoutManager(getContext());
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
        mUseCaseData.putInteger(UseCaseData.CONTENT_TYPE, feedType());

        mPresenter.initialize(mUseCaseData);
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        Intent intent = null;
        listItemSelection = pIndex;
        switch (pModel.getDataType()) {
            case MyConstants.Adapter.TYPE_VIDEO:
                intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
                break;
            case MyConstants.Adapter.TYPE_TEXT:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
                break;
            case MyConstants.Adapter.TYPE_AUDIO:
                intent = new Intent(getContext(), AudioDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
                break;
        }
        if (intent != null)
            startActivityForResult(intent, FeedActivity.REQUEST_CODE_POST_DETAIL);
    }

    @Override
    public void onRefresh() {
        loadPostsList(INITIAL_OFFSET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK) {
            if (requestCode == FeedActivity.REQUEST_CODE_POST_DETAIL) {
                int shareCount = data.getIntExtra(MyConstants.Extras.KEY_SHARE_COUNT, 0);
                mListAdapter.getDataCollection().get(listItemSelection).setShare(shareCount);
                int favCount = data.getIntExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, 0);
                mListAdapter.getDataCollection().get(listItemSelection).setLikes(favCount);
                mListAdapter.notifyDataSetChanged();
            }
        }
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
}
