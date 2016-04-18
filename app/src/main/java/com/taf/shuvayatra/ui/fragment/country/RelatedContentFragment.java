package com.taf.shuvayatra.ui.fragment.country;

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
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.di.component.DaggerDataComponent;
import com.yipl.nrna.di.module.DataModule;
import com.yipl.nrna.domain.model.Post;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.RelatedContentFragmentPresenter;
import com.yipl.nrna.ui.adapter.ListAdapter;
import com.yipl.nrna.ui.interfaces.ListClickCallbackInterface;
import com.yipl.nrna.ui.interfaces.RelatedContentFragmentView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Nirazan-PC on 12/21/2015.
 */
public class RelatedContentFragment extends BaseFragment implements RelatedContentFragmentView {

    @Inject
    RelatedContentFragmentPresenter mPresenter;
    @Bind(R.id.post_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.tvNoPost)
    TextView tvNoPost;
    @Bind(R.id.noPost)
    RelativeLayout mEmptyView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    RelativeLayout mContainer;
    private ListAdapter<Post> mListAdapter;
    private long mQuestionId;

    public RelatedContentFragment() {

    }

    public static RelatedContentFragment newInstance(Long pId) {
        Bundle args = new Bundle();
        args.putLong(MyConstants.Extras.KEY_ID, pId);
        RelatedContentFragment fragment = new RelatedContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_post_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setUpAdapter();
        loadRelatedPosts();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mQuestionId = bundle.getLong(MyConstants.Extras.KEY_ID);
        }
    }

    private void loadRelatedPosts() {
        mPresenter.initialize();
    }

    private void setUpAdapter() {
        mListAdapter = new ListAdapter(getContext(), (ListClickCallbackInterface) getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void initialize() {
        tvNoPost.setText(getString(R.string.sorry_prefix, getString(R.string.empty_post)));
        DaggerDataComponent.builder()
                .dataModule(new DataModule(mQuestionId))
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void renderRelatedContent(List<Post> pPosts) {
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
        Snackbar.make(mContainer, pErrorMessage, Snackbar.LENGTH_SHORT).show();

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
