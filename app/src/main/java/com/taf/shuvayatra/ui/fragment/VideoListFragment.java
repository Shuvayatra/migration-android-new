package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
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
import com.yipl.nrna.ui.CustomRecyclerViewItemDecoration;
import com.yipl.nrna.ui.adapter.ListAdapter;
import com.yipl.nrna.ui.interfaces.ListClickCallbackInterface;
import com.yipl.nrna.ui.interfaces.PostListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nirazan-PC on 12/14/2015.
 */
public class VideoListFragment extends ContentListFragment implements PostListView {

    @Bind(R.id.recylerViewVideoList)
    RecyclerView mRecyclerView;
    @Bind(R.id.tvNoVideo)
    TextView tvNoVideo;
    @Bind(R.id.noVideo)
    RelativeLayout mEmptyView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    RelativeLayout mContainer;

    Integer mType = MyConstants.VideoAdapterType.TYPE_LIST;
    boolean mIncludeChildContents = false;
    private Long mQuestionId = Long.MIN_VALUE;

    public VideoListFragment() {
        super();
        mType = MyConstants.VideoAdapterType.TYPE_GRID;
    }

    public static VideoListFragment newInstance(Long pId, boolean pIncludeChildContents) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(MyConstants.Extras.KEY_ID, pId);
        bundle.putBoolean(MyConstants.Extras.KEY_INCLUDE_CHILD_CONTENTS, pIncludeChildContents);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video_list;
    }

    @Override
    public void showNewContentInfo() {
        Snackbar.make(mContainer, getString(R.string.message_content_available), Snackbar
                .LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        loadVideoList();
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
            mPosts = (List<Post>) savedInstanceState.getSerializable(MyConstants.Extras.KEY_LIST);
            mListAdapter.setDataCollection(postList, mType == MyConstants.VideoAdapterType.TYPE_GRID);
        } else
            loadVideoList();
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

    private void loadVideoList() {
        mPresenter.initialize();
    }

    private void setUpAdapter() {
        if (mType == MyConstants.VideoAdapterType.TYPE_LIST) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.addItemDecoration(new CustomRecyclerViewItemDecoration());
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        }
        mListAdapter = new ListAdapter<Post>(getContext(), new ArrayList<Post>(), (ListClickCallbackInterface) getActivity());
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void initialize() {
        if (mQuestionId != Long.MIN_VALUE) {
            tvNoVideo.setText(getString(R.string.sorry_prefix, getString(R.string
                    .question_related_text, getString(R.string.empty_video))));
            DaggerDataComponent.builder()
                    .dataModule(new DataModule(mQuestionId, MyConstants.DataParent.QUESTION,
                            MyConstants.PostType.VIDEO, false, mIncludeChildContents))
                    .activityModule(((BaseActivity) getActivity()).getActivityModule())
                    .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                    .build()
                    .inject(this);
        } else {
            tvNoVideo.setText(getString(R.string.sorry_prefix, getString(R.string.empty_article)));
            DaggerDataComponent.builder()
                    .dataModule(new DataModule(MyConstants.PostType.VIDEO, false))
                    .activityModule(((BaseActivity) getActivity()).getActivityModule())
                    .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                    .build()
                    .inject(this);
        }
        mPresenter.attachView(this);
    }

    @Override
    public void showLoadingView() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mProgressBar.setVisibility(View.INVISIBLE);
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

    @Override
    public void renderPostList(List<Post> pVideos) {
        mPosts = pVideos;
        mListAdapter.setDataCollection(pVideos, mType == MyConstants.VideoAdapterType.TYPE_GRID);
        List<String> lastTagChoices = ((BaseActivity) getActivity()).getPreferences().getFilterTagChoices();
        List<String> lastStageChoices = ((BaseActivity) getActivity()).getPreferences().getFilterStageChoices();
        filterContentList(lastStageChoices, lastTagChoices);
    }
}
