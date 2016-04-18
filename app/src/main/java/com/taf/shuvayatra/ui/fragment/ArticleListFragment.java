package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yipl.nrna.R;
import com.yipl.nrna.base.BaseActivity;
import com.yipl.nrna.base.ContentListFragment;
import com.yipl.nrna.di.component.DaggerDataComponent;
import com.yipl.nrna.di.module.DataModule;
import com.yipl.nrna.domain.model.Post;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.PostListPresenter;
import com.yipl.nrna.ui.adapter.ListAdapter;
import com.yipl.nrna.ui.interfaces.ListClickCallbackInterface;
import com.yipl.nrna.ui.interfaces.PostListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nirazan-PC on 12/14/2015.
 */
public class ArticleListFragment extends ContentListFragment implements PostListView {

    @Inject
    PostListPresenter mPresenter;

    @Bind(R.id.recyclerViewArticleList)
    RecyclerView mRecyclerView;
    @Bind(R.id.tvNoArticle)
    TextView tvNoArticle;
    @Bind(R.id.noArticle)
    RelativeLayout mEmptyView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    RelativeLayout mContainer;

    Long mQuestionId = Long.MIN_VALUE;
    boolean mIncludeChildContents = false;

    public ArticleListFragment() {
        super();
    }

    public static ArticleListFragment newInstance(Long pId, boolean includeChildContents) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(MyConstants.Extras.KEY_ID, pId);
        bundle.putBoolean(MyConstants.Extras.KEY_INCLUDE_CHILD_CONTENTS, includeChildContents);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_article_list;
    }

    @Override
    public void showNewContentInfo() {
        Snackbar.make(mContainer, getString(R.string.message_content_available), Snackbar
                .LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        loadArticleList();
                    }
                })
                .show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setUpAdapter();
        if (savedInstanceState != null) {
            List<Post> postList = (List<Post>) savedInstanceState.getSerializable(MyConstants.Extras.KEY_FILTERED_LIST);
            mListAdapter.setDataCollection(postList);
            mPosts = (List<Post>) savedInstanceState.getSerializable(MyConstants.Extras.KEY_LIST);
        } else
            loadArticleList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mQuestionId = bundle.getLong(MyConstants.Extras.KEY_ID);
            mIncludeChildContents = bundle.getBoolean(MyConstants.Extras
                    .KEY_INCLUDE_CHILD_CONTENTS, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void loadArticleList() {
        mPresenter.initialize();
    }

    private void setUpAdapter() {
        mListAdapter = new ListAdapter<Post>(getContext(), new ArrayList<Post>(), (ListClickCallbackInterface) getActivity());
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void initialize() {
        if (mQuestionId != Long.MIN_VALUE) {
            tvNoArticle.setText(getString(R.string.sorry_prefix, getString(R.string
                    .question_related_text, getString(R.string.empty_article))));
            DaggerDataComponent.builder()
                    .dataModule(new DataModule(mQuestionId, MyConstants.DataParent.QUESTION,
                            MyConstants.PostType.TEXT, false, mIncludeChildContents))
                    .activityModule(((BaseActivity) getActivity()).getActivityModule())
                    .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                    .build()
                    .inject(this);
        } else {
            tvNoArticle.setText(getString(R.string.sorry_prefix, getString(R.string.empty_article)));
            DaggerDataComponent.builder()
                    .dataModule(new DataModule(MyConstants.PostType.TEXT, false))
                    .activityModule(((BaseActivity) getActivity()).getActivityModule())
                    .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                    .build()
                    .inject(this);
        }
        mPresenter.attachView(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void renderPostList(List<Post> pPosts) {
        mPosts = pPosts;
        mListAdapter.setDataCollection(pPosts);
    }

    @Override
    public void showLoadingView() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {

    }

    @Override
    public void hideRetryView() {

    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mRecyclerView, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideErrorView() {

    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        mEmptyView.setVisibility(View.GONE);
    }

}
