package com.taf.shuvayatra.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Category;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.LatestContentPresenter;
import com.taf.shuvayatra.presenter.PostListPresenter;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.InfoDetailActivity;
import com.taf.shuvayatra.ui.activity.PlacesDetailActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.adapter.CustomArrayAdapter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.LatestContentView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.interfaces.PostListView;
import com.taf.util.MyConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class FeedFragment extends BaseFragment implements
        ListItemClickListener,
        PostListView,
        LatestContentView,
        AdapterView.OnItemSelectedListener,
        SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener {

    public static final Integer PAGE_LIMIT = 12;
    public static final Integer INITIAL_OFFSET = 0;

    @Inject
    LatestContentPresenter mLatestPresenter;
    @Inject
    PostListPresenter mPresenter;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    @Bind(R.id.search_filter)
    LinearLayout mSearchFilterSection;
    @Bind(R.id.search)
    SearchView mSearchView;
    @Bind(R.id.filterSpinner)
    Spinner mFilterSpinner;
    @Bind(R.id.filterContainer)
    CardView mFilterContainer;
    ListAdapter<Post> mListAdapter;
    LinearLayoutManager mLayoutManager;

    boolean mFavouritesOnly = false;
    boolean mFromCategory = false;
    List<Category> mSubCategories;
    List<String> mExcludeTypes;
    List<Post> mPosts;
    String[] mFilters;

    boolean mIsLoading = false, mIsLastPage = false;
    Integer mTotalDataCount = 0;
    Integer mPage = 0;
    UseCaseData mUseCaseData = new UseCaseData();
    private Long mCategoryId;
    private int mCurrentSelection;

    public FeedFragment() {
    }

    public static FeedFragment newInstance(boolean favouritesOnly) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle data = new Bundle();
        data.putBoolean(MyConstants.Extras.KEY_FAVOURITES_ONLY, favouritesOnly);
        feedFragment.setArguments(data);
        return feedFragment;
    }

    public static FeedFragment newInstance(boolean fromCategory, long categoryId, List<Category>
            pCategories, List<String> excludeTypes) {
        FeedFragment feedFragment = new FeedFragment();

        Bundle data = new Bundle();
        data.putBoolean(MyConstants.Extras.KEY_FROM_CATEGORY, fromCategory);
        data.putLong(MyConstants.Extras.KEY_CATEGORY, categoryId);
        data.putSerializable(MyConstants.Extras.KEY_SUBCATEGORY, (Serializable) pCategories);
        data.putSerializable(MyConstants.Extras.KEY_EXCLUDE_LIST, (Serializable) excludeTypes);
        feedFragment.setArguments(data);
        return feedFragment;
    }

    public static FeedFragment newInstance(List<String> pExcludeTypes) {
        Bundle args = new Bundle();
        args.putSerializable(MyConstants.Extras.KEY_EXCLUDE_LIST, (Serializable) pExcludeTypes);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_feed;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSearchFilterSection.setVisibility(mFavouritesOnly ? View.GONE : View.VISIBLE);

        Logger.d("FeedFragment_onActivityCreated", "excludeTypes:" + mExcludeTypes);
        initialize();
        setUpAdapter();
        if (mFavouritesOnly || getContext() instanceof InfoDetailActivity) {
            mFilterContainer.setVisibility(View.GONE);
        } else {
            mFilterContainer.setVisibility(View.VISIBLE);
            loadFilterOptions();
            mFilterSpinner.setOnItemSelectedListener(this);
        }
        loadPostsList(INITIAL_OFFSET);
    }

    private void initialize() {
        DataModule dataModule;
        if (mFavouritesOnly) {
            dataModule = new DataModule(mFavouritesOnly, false);
        } else if (mFromCategory) {
            dataModule = new DataModule(mCategoryId, mExcludeTypes);
        } else {
            dataModule = new DataModule(mExcludeTypes);
        }
        DaggerDataComponent.builder()
                .dataModule(dataModule)
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mLatestPresenter.attachView(this);
        mSearchView.setOnQueryTextListener(this);
    }

    private void setUpAdapter() {
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

                int topRowVerticalPosition = (mRecyclerView == null || mRecyclerView
                        .getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeContainer.setEnabled(topRowVerticalPosition >= 0);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if (!mIsLoading && !mIsLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                            firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_LIMIT) {
                        loadPostsList(mPage);
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        if (mFavouritesOnly) {
            mRecyclerView.setEmptyMessage(getString(R.string.no_favourites));
        }
    }

    private void loadPostsList(Integer pPage) {
        mPage = pPage;
        mSwipeContainer.setRefreshing(true);
        mUseCaseData.clearAll();
        mUseCaseData.putInteger(UseCaseData.OFFSET, pPage * PAGE_LIMIT);
        mUseCaseData.putInteger(UseCaseData.LIMIT, PAGE_LIMIT);
        mPresenter.initialize(mUseCaseData);
    }

    @Override
    public void onRefresh() {
        UseCaseData data = new UseCaseData();
        data.putLong(UseCaseData.LAST_UPDATED, ((BaseActivity) getActivity()).getPreferences()
                .getLastUpdateStamp());
        mLatestPresenter.initialize(data);
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        Intent intent = null;
        mCurrentSelection = pIndex;
        switch (pModel.getDataType()) {
            case MyConstants.Adapter.TYPE_VIDEO:
                intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_VIDEO, pModel);
                break;
            case MyConstants.Adapter.TYPE_TEXT:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ARTICLE, pModel);
                break;
            case MyConstants.Adapter.TYPE_NEWS:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ARTICLE, pModel);
                break;
            case MyConstants.Adapter.TYPE_AUDIO:
                intent = new Intent(getContext(), AudioDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_AUDIO, pModel);
                break;
            case MyConstants.Adapter.TYPE_PLACE:
                intent = new Intent(getContext(), PlacesDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_PLACE, pModel);
                break;
        }
        if (intent != null)
            startActivityForResult(intent, 3209);
    }

    @Override
    public void onListItemSelected(List<BaseModel> pCollection, int pIndex) {
    }

    @Override
    public void latestContentFetched(boolean hasNewContent) {
        loadPostsList(INITIAL_OFFSET);
    }

    @Override
    public void renderPostList(List<Post> pPosts, int pTotalCount) {
        if (mPage == INITIAL_OFFSET) {
            mListAdapter.setDataCollection(pPosts);
            mPosts = pPosts;
        } else {
            mListAdapter.addDataCollection(pPosts);
            mPosts.addAll(pPosts);
        }

        mTotalDataCount = pTotalCount;
        mPage++;
        mIsLastPage = (mPage * PAGE_LIMIT >= pTotalCount);
    }

    @Override
    public void showLoadingView() {
        mIsLoading = true;
        mSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mIsLoading = false;
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    void loadFilterOptions() {
        if (mFromCategory) {
            if (!mSubCategories.isEmpty() && !mSubCategories.get(0).getTitle().equals("All")) {
                Category category = new Category();
                category.setTitle("All");
                mSubCategories.add(0, category);
                Logger.e("FeedFragment", "showing filterlist" + mSubCategories);
                CustomArrayAdapter adapter = new CustomArrayAdapter(getContext(), mSubCategories);
                mFilterSpinner.setAdapter(adapter);
            } else {
                mFilterContainer.setVisibility(View.GONE);
            }
        } else {
            mFilters = getResources().getStringArray(R.array.home_filter_options);
            CustomArrayAdapter adapter = new CustomArrayAdapter(getContext(), mFilters);
            mFilterSpinner.setAdapter(adapter);
        }
    }

    void filterPost(int position) {
        if (mPosts != null) {
            if (position == 0) {
                mListAdapter.setDataCollection(mPosts);
            } else {
                List<Post> filteredPost = new ArrayList<>();
                if (mFromCategory) {
                    for (Post post : mPosts) {
                        Category category = mSubCategories.get(position);
                        Logger.e("FeedFragment", "category list: " + post.getCategoryList());
                        List<Category> postCategories = post.getCategoryList();
                        for (Category postCategory : postCategories) {
                            if (postCategory.getId() == category.getId())
                                filteredPost.add(post);
                        }
                    }
                } else {
                    String option = mFilters[position];
                    if (option.equals(getString(R.string.favourite))) {
                        for (Post post : mPosts) {
                            if (post.isFavourite() != null && post.isFavourite())
                                filteredPost.add(post);
                        }
                    } else {
                        if (option.equals(getString(R.string.article))) {
                            filteredPost = filterByType(getString(R.string.filter_article));
                        } else if (option.equals(getString(R.string.audio))) {
                            filteredPost = filterByType(getString(R.string.filter_audio));
                        } else if (option.equals(getString(R.string.video))) {
                            filteredPost = filterByType(getString(R.string.filter_video));
                        } else if (option.equals(getString(R.string.news))) {
                            filteredPost = filterByType(getString(R.string.filter_news));
                        }
                    }
                }
                mListAdapter.setDataCollection(filteredPost);
            }
        }
    }

    List<Post> filterByType(String type) {
        List<Post> filteredPost = new ArrayList<>();
        for (Post post : mPosts) {
            if (post.getType().toLowerCase().equals(type.toLowerCase()))
                filteredPost.add(post);
        }
        return filteredPost;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.filterSpinner:
                filterPost(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filterBySearchView(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterBySearchView(newText);
        return true;
    }

    void filterBySearchView(String query) {
        if (mPosts != null) {
            List<Post> filteredPost = new ArrayList<>();

            for (Post post : mPosts) {
                if (post.getTitle().toLowerCase().contains(query.toLowerCase()))
                    filteredPost.add(post);
            }
            mListAdapter.setDataCollection(filteredPost);
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK) {
            if (requestCode == 3209) {
                boolean status = data.getBooleanExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS,
                        false);
                int viewCount = data.getIntExtra(MyConstants.Extras.KEY_VIEW_COUNT, 0);
                Logger.e("FeedFragment", "view count = " + viewCount);
                if (viewCount != 0) {
                    mListAdapter.getDataCollection().get(mCurrentSelection).setUnSyncedViewCount(viewCount);
                }
                mListAdapter.getDataCollection().get(mCurrentSelection).setIsFavourite(status);
                mListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mFavouritesOnly = data.getBoolean(MyConstants.Extras.KEY_FAVOURITES_ONLY, false);
            mFromCategory = data.getBoolean(MyConstants.Extras.KEY_FROM_CATEGORY, false);
            mCategoryId = data.getLong(MyConstants.Extras.KEY_CATEGORY);
            mSubCategories = (List<Category>) data.getSerializable(MyConstants.Extras.KEY_SUBCATEGORY);
            mExcludeTypes = (List<String>) data.getSerializable(MyConstants.Extras.KEY_EXCLUDE_LIST);
        }
    }
}
