package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.model.ScreenDataModel;
import com.taf.model.ScreenModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.ScreenDataPresenter;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.ScreenDataView;
import com.taf.util.MyConstants;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by umesh on 1/14/17.
 */

public class FeedScreenFragment extends BaseFragment implements ScreenDataView, ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "FeedScreenFragment";
    private static final String STATE_SCREEN = "screen";
    private static final String STATE_FEEDS = "feeds";
    public static final String STATE_PAGE = "page";
    public static final String STATE_ISLAST_PAGE = "is-last-page";
    public static final Integer INITIAL_OFFSET = 1;
    public static final int REQUEST_CODE_POST_DETAIL = 3209;

    private ScreenModel mScreen;
    ListAdapter<Post> mAdapter;

    @Inject
    ScreenDataPresenter mPresenter;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    LinearLayoutManager mLayoutManager;
    UseCaseData mUseCaseData = getUserCredentialsUseCase();
    int mPage = INITIAL_OFFSET;
    int mTotalDataCount = 0;
    int listItemSelection;
    boolean mIsLoading = false;
    boolean mIsLastPage = false;

    public static FeedScreenFragment newInstance(ScreenModel screenModel) {

        FeedScreenFragment fragment = new FeedScreenFragment();
        fragment.mScreen = screenModel;
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.item_empty_recycler_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpAdapter();
        List<Post> posts = null;
        if (savedInstanceState != null) {
            mScreen = (ScreenModel) savedInstanceState.get(STATE_SCREEN);
            mPage = savedInstanceState.getInt(STATE_PAGE);
            mIsLastPage = savedInstanceState.getBoolean(STATE_ISLAST_PAGE);
            posts = (List<Post>) savedInstanceState.get(STATE_FEEDS);
            mAdapter.setDataCollection(posts);
        }
        initialize();

        mUseCaseData.putString(UseCaseData.SCREEN_DATA_TYPE, mScreen.getType());
        if (posts == null) {
            loadPosts(INITIAL_OFFSET);
        }
    }

    private void initialize() {
        DaggerDataComponent.builder().applicationComponent(getTypedActivity().getApplicationComponent())
                .activityModule(getTypedActivity().getActivityModule())
                .dataModule(new DataModule(mScreen.getId()))
                .build()
                .inject(this);

        mPresenter.attachView(this);
    }

    private void setUpAdapter() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListAdapter<Post>(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);

        mSwipeRefreshLayout.setOnRefreshListener(this);

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
                        loadPosts(mPage + 1);
                    }
                }
            }
        });
    }

    private void loadPosts(Integer pPage) {
        Logger.e(TAG, "pPage: " + pPage);
        mSwipeRefreshLayout.setRefreshing(true);
        mUseCaseData.putInteger(UseCaseData.NEXT_PAGE, pPage);
        mPresenter.initialize(mUseCaseData);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingView() {
        mIsLoading = true;
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mIsLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void renderScreenData(ScreenDataModel model) {

        if (model.isFromCache()) {
            if (mAdapter.getDataCollection().isEmpty()) {
                mAdapter.setDataCollection(model.getData());
                mPage = model.getCurrentPage();
                mIsLastPage = mPage == model.getLastPage();
                Logger.e(TAG, "cache: page " + mPage + " last page " + model.getTotalCount());
                Logger.e(TAG, "cache: page " + mAdapter.getDataCollection().size());
            }
            return;
        }

        Logger.e(TAG, " ============================ start ==================================");
        Logger.e(TAG, "current page / total page" + model.getCurrentPage() + " / " + model.getLastPage());
        Logger.e(TAG, "total items: " + model.getTotalCount());
        Logger.e(TAG, "prevoius item: " + mAdapter.getItemCount());
        Logger.e(TAG, "add items:  " + model.getData().size());
        mPage = model.getCurrentPage();

        if (mPage == INITIAL_OFFSET) {
            mAdapter.setDataCollection(model.getData());
        } else {
            mAdapter.addDataCollection(model.getData());
        }
        mAdapter.setDataCollection(model.getData());

        mIsLastPage = (mPage == model.getLastPage());
        Logger.e(TAG, "new items " + mAdapter.getItemCount());
        Logger.e(TAG, " ============================ end ================================== \n");

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
            startActivityForResult(intent, REQUEST_CODE_POST_DETAIL);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_POST_DETAIL) {
                int shareCount = data.getIntExtra(MyConstants.Extras.KEY_SHARE_COUNT, 0);
                mAdapter.getDataCollection().get(listItemSelection).setShare(shareCount);
                int favCount = data.getIntExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, 0);
                mAdapter.getDataCollection().get(listItemSelection).setLikes(favCount);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRefresh() {
        mPage = INITIAL_OFFSET;
        loadPosts(INITIAL_OFFSET);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_SCREEN, mScreen);
        outState.putSerializable(STATE_FEEDS, (Serializable) mAdapter.getDataCollection());
        outState.putSerializable(STATE_ISLAST_PAGE, mIsLastPage);
        outState.putSerializable(STATE_PAGE, mPage);
        super.onSaveInstanceState(outState);
    }
}
