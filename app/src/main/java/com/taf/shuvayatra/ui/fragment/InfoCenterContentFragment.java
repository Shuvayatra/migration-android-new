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
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.di.component.DaggerDataComponent;
import com.yipl.nrna.di.module.DataModule;
import com.yipl.nrna.domain.model.Post;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.PostListPresenter;
import com.yipl.nrna.ui.activity.MainActivity;
import com.yipl.nrna.ui.adapter.ListAdapter;
import com.yipl.nrna.ui.interfaces.ListClickCallbackInterface;
import com.yipl.nrna.ui.interfaces.PostListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InfoCenterContentFragment extends BaseFragment implements
        PostListView {

    @Inject
    PostListPresenter mPresenter;

    @Bind(R.id.postList)
    RecyclerView mRecyclerView;
    @Bind(R.id.tvNoPost)
    TextView tvNoPost;
    @Bind(R.id.noPost)
    RelativeLayout mEmptyView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.main_container)
    RelativeLayout mContainer;
    private MyConstants.Stage mStage;
    private ListAdapter<Post> mListAdapter;

    public InfoCenterContentFragment() {
        // Required empty public constructor
    }

    public static InfoCenterContentFragment newInstance(MyConstants.Stage stage) {
        Bundle args = new Bundle();
        args.putSerializable(MyConstants.Extras.KEY_STAGE, stage);
        InfoCenterContentFragment fragment = new InfoCenterContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_info_center_content;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setElevation(0);
        initialize();
        setUpAdapter();
        loadRelatedPosts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void showNewContentInfo() {
        Snackbar.make(mContainer, "", Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        loadRelatedPosts();
                    }
                })
                .show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStage = (MyConstants.Stage) getArguments().getSerializable(MyConstants.Extras.KEY_STAGE);
        if (mStage == null) {
            throw new IllegalStateException("Fragment argument must consist of Stage.");
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

    private void initialize() {
        tvNoPost.setText(getString(R.string.sorry_prefix, getString(R.string.stage_related_text,
                getString(R.string.empty_post))));
        DaggerDataComponent.builder()
                .dataModule(new DataModule(mStage))
                .activityModule(((MainActivity) getActivity()).getActivityModule())
                .applicationComponent(((MainActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    private void setUpAdapter() {
        mListAdapter = new ListAdapter(getContext(), (ListClickCallbackInterface) getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void loadRelatedPosts() {
        mPresenter.initialize();
    }

    @Override
    public void renderPostList(List<Post> pPosts) {
        if (pPosts != null)
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
