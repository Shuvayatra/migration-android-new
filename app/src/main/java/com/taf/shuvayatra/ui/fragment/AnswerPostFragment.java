package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.yipl.nrna.R;
import com.yipl.nrna.base.BaseActivity;
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.databinding.ArticleDataBinding;
import com.yipl.nrna.databinding.AudioDataBinding;
import com.yipl.nrna.databinding.VideoDataBinding;
import com.yipl.nrna.di.component.DaggerDataComponent;
import com.yipl.nrna.di.module.DataModule;
import com.yipl.nrna.domain.model.Answer;
import com.yipl.nrna.domain.model.Post;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.AnswerPostListPresenter;
import com.yipl.nrna.ui.activity.ArticleDetailActivity;
import com.yipl.nrna.ui.activity.VideoDetailActivity;
import com.yipl.nrna.ui.interfaces.PostListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerPostFragment extends BaseFragment implements PostListView {

    @Inject
    AnswerPostListPresenter mPresenter;

    @Bind(R.id.post_container)
    RelativeLayout mPostContainer;
    @Bind(R.id.toggleList)
    ImageView mToggleList;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.postList)
    LinearLayout mPostList;
    @Bind(R.id.no_posts)
    TextView mNoPost;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    LinearLayout mContainer;

    private List<Post> mPosts;

    private Long mAnswerId = Long.MIN_VALUE;
    private String mAnswerTitle;

    public AnswerPostFragment() {
        super();
    }

    public static AnswerPostFragment newInstance(Answer pAnswer) {
        AnswerPostFragment fragment = new AnswerPostFragment();
        Bundle data = new Bundle();
        data.putLong(MyConstants.Extras.KEY_ID, pAnswer.getId());
        data.putString(MyConstants.Extras.KEY_TITLE, pAnswer.getTitle());
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_answer_post;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mToggleList.setImageDrawable(new IconicsDrawable(getContext(), GoogleMaterial.Icon
                .gmd_add).color(getResources().getColor(R.color.text_color_primary))
                .actionBar());
        mTitle.setText(mAnswerTitle);
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mAnswerId = data.getLong(MyConstants.Extras.KEY_ID);
            mAnswerTitle = data.getString(MyConstants.Extras.KEY_TITLE);
        } else {
            throw new IllegalStateException("Argument data is necessary...");
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

    @OnClick(R.id.toggleList)
    public void togglePostListVisibility() {
        if (mPostContainer.getVisibility() == View.VISIBLE) {
            mPostContainer.setVisibility(View.GONE);
            mToggleList.setImageDrawable(new IconicsDrawable(getContext(), GoogleMaterial.Icon
                    .gmd_add).color(getResources().getColor(R.color.text_color_primary))
                    .actionBar());
        } else {
            mPostContainer.setVisibility(View.VISIBLE);
            mToggleList.setImageDrawable(new IconicsDrawable(getContext(), GoogleMaterial.Icon
                    .gmd_remove).color(getResources().getColor(R.color.text_color_primary))
                    .actionBar());

            if (mPostList.getChildCount() == 0) {
                loadAudioList();
            }
        }
    }

    private void loadAudioList() {
        mPresenter.initialize();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule(mAnswerId, MyConstants.DataParent.ANSWER, null, false,
                        false))
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
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
        Snackbar.make(mPostContainer, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideErrorView() {

    }

    @Override
    public void showEmptyView() {
        mNoPost.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        mNoPost.setVisibility(View.INVISIBLE);
    }

    @Override
    public void renderPostList(List<Post> pPosts) {
        if (pPosts != null) {
            mPosts = pPosts;
            for (Post post : mPosts) {
                if (post.getDataType() == MyConstants.Adapter.TYPE_AUDIO) {
                    renderAudio(post);
                } else if (post.getDataType() == MyConstants.Adapter.TYPE_VIDEO) {
                    renderVideo(post);
                } else if (post.getDataType() == MyConstants.Adapter.TYPE_TEXT) {
                    renderArticle(post);
                }
            }
        }
    }

    public void renderAudio(final Post pAudio) {
        if (pAudio != null) {
            AudioDataBinding audioDataBinding = DataBindingUtil.inflate
                    (LayoutInflater.from(getContext()), R.layout.list_item_audio, mPostList,
                            false);
            audioDataBinding.setAudio(pAudio);
            View view = audioDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                /*params.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.spacing_small));*/
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        /*Intent intent = new Intent(getContext(), AudioDetailActivity.class);
                        intent.putExtra(MyConstants.Extras.KEY_AUDIO_LIST, (Serializable) pAudios);
                        intent.putExtra(MyConstants.Extras.KEY_AUDIO_SELECTION, selectionIndex);
                        startActivity(intent);*/
                }
            });
            mPostList.addView(view);
        }
    }

    public void renderVideo(final Post pVideo) {
        if (pVideo != null) {
            VideoDataBinding videoDataBinding = DataBindingUtil.inflate
                    (LayoutInflater.from(getContext()), R.layout.list_item_video, mPostList,
                            false);
            videoDataBinding.setVideo(pVideo);
            View view = videoDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                /*params.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.spacing_small));*/
            view.setLayoutParams(params);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), VideoDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_ID, pVideo.getId());
                    intent.putExtra(MyConstants.Extras.KEY_TITLE, pVideo.getTitle());
                    startActivity(intent);
                }
            });

            mPostList.addView(view);
        }
    }

    public void renderArticle(final Post pArticle) {
        if (pArticle != null) {
            ArticleDataBinding articleDataBinding = DataBindingUtil.inflate
                    (LayoutInflater.from(getContext()), R.layout.list_item_article, mPostList,
                            false);
            articleDataBinding.setArticle(pArticle);
            View view = articleDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                /*params.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.spacing_small));*/
            view.setLayoutParams(params);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_ID, pArticle.getId());
                    startActivity(intent);
                }
            });

            mPostList.addView(view);
        }
    }
}
