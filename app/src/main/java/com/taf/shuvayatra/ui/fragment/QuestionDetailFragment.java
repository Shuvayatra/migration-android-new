package com.taf.shuvayatra.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yipl.nrna.R;
import com.yipl.nrna.base.BaseActivity;
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.data.utils.Logger;
import com.yipl.nrna.databinding.ArticleDataBinding;
import com.yipl.nrna.databinding.AudioDataBinding;
import com.yipl.nrna.databinding.VideoDataBinding;
import com.yipl.nrna.di.component.DaggerDataComponent;
import com.yipl.nrna.di.module.DataModule;
import com.yipl.nrna.domain.model.Post;
import com.yipl.nrna.domain.model.Question;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.PostListPresenter;
import com.yipl.nrna.presenter.QuestionDetailPresenter;
import com.yipl.nrna.ui.activity.ArticleDetailActivity;
import com.yipl.nrna.ui.activity.AudioDetailActivity;
import com.yipl.nrna.ui.activity.QuestionDetailActivity;
import com.yipl.nrna.ui.activity.VideoDetailActivity;
import com.yipl.nrna.ui.interfaces.QuestionDetailActivityView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionDetailFragment extends BaseFragment implements QuestionDetailActivityView {

    @Inject
    QuestionDetailPresenter mPresenter;
    @Inject
    PostListPresenter mPostPresenter;

    @Bind(R.id.scrollView)
    NestedScrollView mScrollView;
    @Bind(R.id.answer)
    WebView mAnswer;
    @Bind(R.id.post_container)
    LinearLayout mPostContainer;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.no_content)
    TextView tvNoPosts;
    @Bind(R.id.read_more)
    TextView mReadMore;
    @Bind(R.id.sub_question_section)
    LinearLayout mSubQuestionSection;
    @Bind(R.id.related_post_section)
    LinearLayout mRelatedPostSection;
    @Bind(R.id.sub_question_container)
    LinearLayout mSubQuestionContainer;

    private List<Post> mPosts;
    private int selectionIndex = 0;

    private Long mQuestionId = Long.MIN_VALUE;

    public QuestionDetailFragment() {
        // Required empty public constructor
    }

    public static QuestionDetailFragment newInstance(Long pId) {
        QuestionDetailFragment fragment = new QuestionDetailFragment();
        Bundle data = new Bundle();
        data.putLong(MyConstants.Extras.KEY_ID, pId);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_question_detail;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mQuestionId = bundle.getLong(MyConstants.Extras.KEY_ID);
        }
    }

    @OnClick(R.id.read_more)
    public void showFullLengthAnswer() {
        ViewGroup.LayoutParams lp = mAnswer.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            lp.height = getResources().getDimensionPixelOffset(R.dimen.content_wrap_height);
            mReadMore.setText(getString(R.string.read_more));
            mScrollView.fullScroll(View.FOCUS_DOWN);
        } else {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mReadMore.setText(getString(R.string.read_less));
        }
        mAnswer.setLayoutParams(lp);
        mAnswer.invalidate();
    }

    private void initialize() {
        if (mQuestionId != Long.MIN_VALUE) {
            DaggerDataComponent.builder()
                    .dataModule(new DataModule(mQuestionId, MyConstants.DataParent.QUESTION,
                            null, false, false))
                    .activityModule(((BaseActivity) getActivity()).getActivityModule())
                    .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                    .build()
                    .inject(this);
            mPresenter.attachView(this);
            mPresenter.initialize();

            mPostPresenter.attachView(this);
            mPostPresenter.initialize();
        } else {
            throw new IllegalStateException("Question Id is required");
        }
    }

    @Override
    public void renderQuestionDetail(Question pQuestion) {
        /*mAnswer.loadDataWithBaseURL(null, pQuestion.getAnswer() != null ? pQuestion
                .getAnswer() : "Content is null", "text/html", "utf-8", null);*/
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
        sb.append(pQuestion.getAnswer() != null ? pQuestion.getAnswer() : "Content is null");
        sb.append("</body></HTML>");
        mAnswer.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html",
                "utf-8", null);

        String[] childIds = pQuestion.getChildIds() != null
                ? pQuestion.getChildIds().split("::")
                : "".split("::");
        String[] childTitles = pQuestion.getChildTitles() != null
                ? pQuestion.getChildTitles().split("::")
                : "".split("::");

        mSubQuestionSection.setVisibility(childIds.length > 1 ? View.VISIBLE : View.GONE);
        if (childIds.length > 1) renderSubQuestions(childIds, childTitles);
    }

    @Override
    public void renderSubQuestions(String[] pChildIds, String[] pChildTitles) {
        if (pChildIds != null) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < pChildIds.length; i++) {
                mSubQuestionContainer.addView(inflateSubQuestionView(inflater,
                        Long.valueOf(pChildIds[i]), pChildTitles[i]));
            }
        }
    }

    private View inflateSubQuestionView(LayoutInflater inflater, final Long pQuestionId, final String
            pQuestionTitle) {
        View view = inflater.inflate(R.layout.list_item_sub_question, mSubQuestionContainer,
                false);
        view.setId(pQuestionId.intValue());
        ((TextView) ButterKnife.findById(view, R.id.title)).setText(pQuestionTitle);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QuestionDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ID, pQuestionId);
                intent.putExtra(MyConstants.Extras.KEY_TITLE, pQuestionTitle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void renderPostList(List<Post> pPosts) {
        if (pPosts != null && pPosts.size() > 0) {
            mPosts = pPosts;
            for (Post post : mPosts) {
                Logger.d("QuestionDetailFragment_renderPostList", "test: " + post.getType());
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
                    (LayoutInflater.from(getContext()), R.layout.list_item_audio, mPostContainer,
                            false);
            audioDataBinding.setAudio(pAudio);
            View view = audioDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AudioDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_AUDIO_LIST, (Serializable)
                            getAudioList(pAudio.getId()));
                    intent.putExtra(MyConstants.Extras.KEY_AUDIO_SELECTION, selectionIndex);
                    startActivity(intent);
                }
            });
            mPostContainer.addView(view);
        }
    }

    private List<Post> getAudioList(long id) {
        List<Post> list = new ArrayList<>();
        for (int i = 0; i < mPosts.size(); i++) {
            if (mPosts.get(i).getDataType() == MyConstants.Adapter.TYPE_AUDIO) {
                list.add(mPosts.get(i));
                selectionIndex = i;
            }
        }
        return list;
    }

    public void renderVideo(final Post pVideo) {
        if (pVideo != null) {
            VideoDataBinding videoDataBinding = DataBindingUtil.inflate
                    (LayoutInflater.from(getContext()), R.layout.list_item_video, mPostContainer,
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

            mPostContainer.addView(view);
        }
    }

    public void renderArticle(final Post pArticle) {
        if (pArticle != null) {
            ArticleDataBinding articleDataBinding = DataBindingUtil.inflate
                    (LayoutInflater.from(getContext()), R.layout.list_item_article, mPostContainer,
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

            mPostContainer.addView(view);
        }
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
        mRelatedPostSection.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        mRelatedPostSection.setVisibility(View.VISIBLE);
    }
}
