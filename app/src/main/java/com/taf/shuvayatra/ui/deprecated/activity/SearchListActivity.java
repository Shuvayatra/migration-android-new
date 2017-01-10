package com.taf.shuvayatra.ui.deprecated.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.deprecated.PostListPresenter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.deprecated.interfaces.PostListView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SearchListActivity extends BaseActivity implements PostListView, ListItemClickListener {

    @Inject
    PostListPresenter mPostListPresenter;

    String mTitle;
    String mTags;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;
    private ListAdapter<Post> mListAdapter;
    private int listItemSelection;

    @Override
    public int getLayout() {
        return R.layout.activity_search_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTitle = bundle.getString(MyConstants.Extras.KEY_TITLE, null);
            mTags = (String) bundle.get(MyConstants.Extras.KEY_TAG);
        }
        getSupportActionBar().setTitle(mTitle != null ? mTitle : mTags);
        Logger.e("SearchListActivity", "title " + mTitle);
        initialize();
        setupAdapter();
        mPostListPresenter.initialize(new UseCaseData());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        List<String> tags = new ArrayList<>();
        if (mTags != null)
            tags.add(mTags);
        DaggerDataComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .dataModule(new DataModule(mTitle, tags))
                .build()
                .inject(this);
        mPostListPresenter.attachView(this);
    }

    private void setupAdapter() {
        mListAdapter = new ListAdapter(this, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public void showErrorView(String pErrorMessage) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void renderPostList(List<Post> pPosts, int pTotalCount) {
        Logger.e("SearchListActivity", "search :" + pPosts);
        mListAdapter.setDataCollection(pPosts);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

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
            case MyConstants.Adapter.TYPE_NEWS:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ARTICLE, pModel);
                break;
            case MyConstants.Adapter.TYPE_AUDIO:
                intent = new Intent(getContext(), AudioDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_AUDIO, pModel);
                break;
        }
        if (intent != null)
            startActivityForResult(intent, 3209);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK) {
            if (requestCode == 3209) {
                boolean status = data.getBooleanExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS,
                        false);
                int viewCount = data.getIntExtra(MyConstants.Extras.KEY_VIEW_COUNT, 0);
                Logger.e("FeedFragment", "view count = " + viewCount);
                if (viewCount != 0) {
                    mListAdapter.getDataCollection().get(listItemSelection).setUnSyncedViewCount(viewCount);
                }
                int shareCount = data.getIntExtra(MyConstants.Extras.KEY_SHARE_COUNT, 0);
                Logger.e("FeedFragment", "share count = " + shareCount);
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

}
