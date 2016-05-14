package com.taf.shuvayatra.ui.activity;

import android.content.Context;
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
import com.taf.shuvayatra.presenter.PostListPresenter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.interfaces.PostListView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class SearchListActivity extends BaseActivity implements PostListView, ListItemClickListener {

    @Inject
    PostListPresenter mPostListPresenter;

    String mTitle;
    String mTags;

    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    private ListAdapter mAdapter;

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
        mAdapter = new ListAdapter(this, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
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
        mAdapter.setDataCollection(pPosts);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {

    }
}
