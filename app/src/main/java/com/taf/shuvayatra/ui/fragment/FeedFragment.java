package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PostListPresenter;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.PlacesDetailActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.adapter.CustomArrayAdapter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.interfaces.PostListView;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class FeedFragment extends BaseFragment implements
        ListItemClickListener,
        PostListView {

    public static final Integer PAGE_LIMIT = 12;
    public static final Integer INITIAL_OFFSET = 0;

    @Inject
    PostListPresenter mPresenter;

    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    @Bind(R.id.search)
    SearchView mSearchView;
    @Bind(R.id.filterSpinner)
    Spinner mFilterSpinner;
    ListAdapter<Post> mListAdapter;
    LinearLayoutManager mLayoutManager;

    boolean mFavouritesOnly = false;
    boolean mFromCategory = false;

    boolean mIsLoading = false, mIsLastPage = false;
    Integer mTotalDataCount = 0;
    Integer mPage = 0;
    UseCaseData mUseCaseData = new UseCaseData();
    private Long mCategoryId;

    @Override
    public int getLayout() {
        return R.layout.fragment_feed;
    }

    public static FeedFragment newInstance(boolean favouritesOnly) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle data = new Bundle();
        data.putBoolean(MyConstants.Extras.KEY_FAVOURITES_ONLY, favouritesOnly);
        feedFragment.setArguments(data);
        return feedFragment;
    }

    public static FeedFragment newInstance(boolean fromCategory, long categoryId) {
        FeedFragment feedFragment = new FeedFragment();

        Bundle data = new Bundle();
        data.putBoolean(MyConstants.Extras.KEY_FROM_CATEGORY, fromCategory);
        data.putLong(MyConstants.Extras.KEY_CATEGORY, categoryId);
        feedFragment.setArguments(data);
        return feedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mFavouritesOnly = data.getBoolean(MyConstants.Extras.KEY_FAVOURITES_ONLY, false);
            mFromCategory = data.getBoolean(MyConstants.Extras.KEY_FROM_CATEGORY, false);
            mCategoryId = data.getLong(MyConstants.Extras.KEY_CATEGORY);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
        setUpAdapter();
        loadFilterOptions();

        loadPostsList(INITIAL_OFFSET);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSearchView.clearFocus();
    }

    private void initialize() {
        DataModule dataModule;
        if (mFavouritesOnly) {
            dataModule = new DataModule(mFavouritesOnly, false);
        } else if (mFromCategory) {
            dataModule = new DataModule(mCategoryId, MyConstants.DataParent.JOURNEY);
        }else{
            dataModule = new DataModule();
        }
        DaggerDataComponent.builder()
                .dataModule(dataModule)
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    private void setUpAdapter() {
        mListAdapter = new ListAdapter(getContext(), this);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
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
                //mSwipeContainer.setEnabled(topRowVerticalPosition >= 0);

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
        mUseCaseData.clearAll();
        mUseCaseData.putInteger(UseCaseData.OFFSET, pPage * PAGE_LIMIT);
        mUseCaseData.putInteger(UseCaseData.LIMIT, PAGE_LIMIT);
        mPresenter.initialize(mUseCaseData);
    }

    @Override
    public void onListItemSelected(BaseModel pModel) {
        Intent intent = null;
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
            startActivity(intent);
    }

    @Override
    public void onListItemSelected(List<BaseModel> pCollection, int pIndex) {
    }

    @Override
    public void renderPostList(List<Post> pPosts, int pTotalCount) {
        if (mPage == INITIAL_OFFSET)
            mListAdapter.setDataCollection(pPosts);
        else
            mListAdapter.addDataCollection(pPosts);

        mTotalDataCount = pTotalCount;
        mPage++;
        mIsLastPage = (mPage * PAGE_LIMIT >= pTotalCount);
    }

    @Override
    public void showLoadingView() {
        mIsLoading = true;
    }

    @Override
    public void hideLoadingView() {
        mIsLoading = false;
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    void loadFilterOptions() {
        if (mFromCategory) {
            List<Category> categories = ((CategoryDetailActivity) getActivity()).getSubCategories();
            if(!categories.isEmpty()&&!categories.get(0).getTitle().equals("All")) {
                Category category = new Category();
                category.setTitle("All");
                categories.add(0, category);
            }
            Logger.e("FeedFragment", "showing filterlist" + categories);
            CustomArrayAdapter adapter = new CustomArrayAdapter(getContext(),categories);
            mFilterSpinner.setAdapter(adapter);
        }
    }
}
