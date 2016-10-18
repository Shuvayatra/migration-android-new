package com.taf.shuvayatra.ui.deprecated.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.TagListDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.deprecated.TagListPresenter;
import com.taf.shuvayatra.ui.deprecated.interfaces.TagListView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TagListActivity extends BaseActivity implements
        TagListView, SearchView.OnQueryTextListener {

    @Inject
    TagListPresenter mPresenter;
    @BindView(R.id.tag_list_container)
    FlowLayout mTagsContainer;
    Boolean mIsQuerySubmitted = false;
    private SearchView mSearchView;

    @Override
    public int getLayout() {
        return R.layout.activity_tag_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();
        mPresenter.initialize(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsQuerySubmitted = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setIconified(false);
        mSearchView.setQueryHint(getString(R.string.query_hint));
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule())
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void renderTagList(List<String> pTags) {
        mTagsContainer.removeAllViews();
        for (final String tag : pTags) {
            TagListDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.view_tag, mTagsContainer, false);
            dataBinding.setTag(tag);
            dataBinding.setSelected(false);
            View view = dataBinding.getRoot();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnalyticsUtil.logSearchEvent(getAnalytics(), tag, true);

                    Intent intent = new Intent(TagListActivity.this, SearchListActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_TAG, tag);
                    startActivity(intent);
                }
            });
            mTagsContainer.addView(view);
        }
    }

    @Override
    public void showLoadingView() {
    }

    @Override
    public void hideLoadingView() {
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mTagsContainer, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Logger.e("TagListActivity", "query submitted");
        if (!mIsQuerySubmitted) {
            mIsQuerySubmitted = true;
            Intent intent = new Intent(this, SearchListActivity.class);
            intent.putExtra(MyConstants.Extras.KEY_TITLE, query);
            startActivity(intent);

            AnalyticsUtil.logSearchEvent(getAnalytics(), query, false);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Logger.e("TagListActivity", "searchlist activity started");
        return true;
    }
}
