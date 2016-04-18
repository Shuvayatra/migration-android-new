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
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.di.component.DaggerDataComponent;
import com.yipl.nrna.di.module.DataModule;
import com.yipl.nrna.domain.model.Answer;
import com.yipl.nrna.domain.model.BaseModel;
import com.yipl.nrna.domain.model.Post;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.AnswerListFragmentPresenter;
import com.yipl.nrna.ui.adapter.FragmentListAdapter;
import com.yipl.nrna.ui.interfaces.AnswerListView;
import com.yipl.nrna.ui.interfaces.ListClickCallbackInterface;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nirazan-PC on 12/14/2015.
 */
public class AnswerListFragment extends BaseFragment implements
        AnswerListView,
        ListClickCallbackInterface {

    protected FragmentListAdapter<Answer> mListAdapter;
    protected List<Answer> mAnswers;
    @Inject
    AnswerListFragmentPresenter mPresenter;
    @Bind(R.id.answer_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.no_answer)
    TextView tvNoArticle;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    RelativeLayout mContainer;
    Long mQuestionId = Long.MIN_VALUE;

    public AnswerListFragment() {
        super();
    }

    public static AnswerListFragment newInstance(Long pId) {
        AnswerListFragment fragment = new AnswerListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(MyConstants.Extras.KEY_ID, pId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_answer_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setUpAdapter();
        loadArticleList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mQuestionId = bundle.getLong(MyConstants.Extras.KEY_ID);
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
        mListAdapter = new FragmentListAdapter<>(getContext(), this);
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void initialize() {
        if (mQuestionId != Long.MIN_VALUE) {
            DaggerDataComponent.builder()
                    .dataModule(new DataModule(mQuestionId))
                    .activityModule(((BaseActivity) getActivity()).getActivityModule())
                    .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                    .build()
                    .inject(this);
        } else {
            DaggerDataComponent.builder()
                    .activityModule(((BaseActivity) getActivity()).getActivityModule())
                    .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                    .build()
                    .inject(this);
        }
        mPresenter.attachView(this);
        tvNoArticle.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void renderAnswerList(List<Answer> pAnswers) {
        mAnswers = pAnswers;
        mListAdapter.setDataCollection(mAnswers);
    }

    @Override
    public void onListItemSelected(BaseModel pModel) {
        //// TODO: 12/25/15
    }

    @Override
    public void onAudioItemSelected(List<Post> pAudios, int index) {
        throw new UnsupportedOperationException();
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
        tvNoArticle.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        tvNoArticle.setVisibility(View.INVISIBLE);
    }

}
