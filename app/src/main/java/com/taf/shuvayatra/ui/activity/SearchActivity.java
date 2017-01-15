package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.model.PostResponse;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.SearchPostListPresenter;
import com.taf.shuvayatra.ui.adapter.DropDownAdapter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.SearchPostListView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Func1;

public class SearchActivity extends BaseActivity implements ListItemClickListener, SearchPostListView, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "SearchActivity";
    public static final Integer PAGE_LIMIT = 15;
    public static final Integer INITIAL_OFFSET = 1;

    @Inject
    SearchPostListPresenter mPresenter;

    @BindView(R.id.search)
    ImageView mSearch;
    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.textview_search)
    EditText mSearchTextBox;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.spinner_search_type)
    Spinner mTypeSpinner;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;


    LinearLayoutManager mLayoutManager;
    ListAdapter<Post> mPostAdapter;
    UseCaseData mUseCaseData;
    Integer mPage = INITIAL_OFFSET;
    Integer mTotalDataCount = 0;
    boolean mIsLoading = false;
    boolean mIsLastPage = false;
    String mQuery;
    String mSearchType = "";

    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
        setupAdapter();
        addListeners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .dataModule(new DataModule())
                .build()
                .inject(this);

        mPresenter.attachView(this);
    }

    private void setupAdapter() {
        mPostAdapter = new ListAdapter(this, this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mUseCaseData = new UseCaseData();

        List<String> searchTypes = new ArrayList<>();
        searchTypes.add(getString(R.string.search_type_header));
        searchTypes.addAll(Arrays.asList(getResources().getStringArray(R.array.search_type)));
        DropDownAdapter typeAdapter = new DropDownAdapter(getContext(), searchTypes);
        mTypeSpinner.setAdapter(typeAdapter);
    }

    private void addListeners() {

        mSwipeRefreshLayout.setOnRefreshListener(this);

        RxTextView.textChangeEvents(mSearchTextBox)
                .debounce(5, TimeUnit.SECONDS)
                .map(new Func1<TextViewTextChangeEvent, Object>() {
                    @Override
                    public Object call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        Logger.e(TAG, "mIsLoading: " + mIsLoading);
                        Logger.e(TAG, "mQuery " + mQuery.equals(mSearchTextBox.getText().toString()));
                        if (!mQuery.equals(mSearchTextBox.getText().toString())) {
                            mQuery = mSearchTextBox.getText().toString();
                            searchPosts(INITIAL_OFFSET, mQuery, mSearchType);
                        }
                        return null;
                    }
                }).subscribe();

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mSearchTextBox.getText().toString();
                mSwipeRefreshLayout.setRefreshing(true);

                searchPosts(INITIAL_OFFSET, mQuery, mSearchType);
            }
        });

        mSearchTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mQuery = mSearchTextBox.getText().toString();
                    mSwipeRefreshLayout.setRefreshing(true);
                    searchPosts(INITIAL_OFFSET, mQuery, mSearchType);
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                }
                return false;
            }
        });

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = (String) mTypeSpinner.getSelectedItem();
                if (position == 0) {
                    mSearchType = "";
                } else if (type.equals(getString(R.string.audio))) {
                    mSearchType = "audio";
                } else if (type.equals(getString(R.string.video))) {
                    mSearchType = "video";
                } else if (type.equals(getString(R.string.article))) {
                    mSearchType = "text";
                }

                if (mQuery != null) {
                    mQuery = mSearchTextBox.getText().toString();
                    mSwipeRefreshLayout.setRefreshing(true);
                    searchPosts(INITIAL_OFFSET, mQuery, mSearchType);
                } else {
                    mQuery = mSearchTextBox.getText().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                        Logger.e(TAG, "mPage: " + mPage);
                        mSwipeRefreshLayout.setRefreshing(true);
                        searchPosts(mPage + 1, mQuery, mSearchType);
                    }
                }
            }
        });

    }

    private void searchPosts(int page, String query, String type) {
        mPage = page;
        mPresenter.destroy();       //cancel any pending query before initating new one
        mUseCaseData.putInteger(UseCaseData.OFFSET, page);
        mUseCaseData.putInteger(UseCaseData.LIMIT, PAGE_LIMIT);
        mUseCaseData.putString(UseCaseData.SEARCH_QUERY, query);
        mUseCaseData.putString(UseCaseData.SEARCH_TYPE, type);

        mPresenter.initialize(mUseCaseData);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        Intent intent = null;
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
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void renderPosts(PostResponse postResponse) {
        Logger.e(TAG, "pagination: " + mPostAdapter.getItemCount());
        if (mPage == INITIAL_OFFSET) {
            mPostAdapter.setDataCollection(postResponse.getData());
        } else {
            mPostAdapter.addDataCollection(postResponse.getData());
        }
        mTotalDataCount = postResponse.getTotal();
        mIsLastPage = (mPage == postResponse.getLastPage());
        mPage = postResponse.getCurrentPage();
        Logger.e(TAG, "pagination: " + mPostAdapter.getItemCount());
    }

    @Override
    public void showLoadingView() {
        mIsLoading = true;
    }

    @Override
    public void hideLoadingView() {
        mIsLoading = false;
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        searchPosts(INITIAL_OFFSET, mSearchTextBox.getText().toString(), mSearchType);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
