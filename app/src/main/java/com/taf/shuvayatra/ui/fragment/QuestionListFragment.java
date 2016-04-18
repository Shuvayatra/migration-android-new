package com.taf.shuvayatra.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yipl.nrna.R;
import com.yipl.nrna.base.BaseActivity;
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.di.component.DaggerDataComponent;
import com.yipl.nrna.di.module.DataModule;
import com.yipl.nrna.domain.model.Question;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.QuestionListFragmentPresenter;
import com.yipl.nrna.ui.adapter.ListAdapter;
import com.yipl.nrna.ui.interfaces.ListClickCallbackInterface;
import com.yipl.nrna.ui.interfaces.QuestionListFragmentView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Nirazan-PC on 12/15/2015.
 */
public class QuestionListFragment extends BaseFragment implements QuestionListFragmentView {

    @Inject
    QuestionListFragmentPresenter mPresenter;
    @Bind(R.id.recyclerViewQuestionList)
    RecyclerView mRecyclerView;
    @Bind(R.id.tvNoQuestion)
    TextView tvNoQuestion;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    RelativeLayout mContainer;

    ListAdapter<Question> mListAdapter;
    List<Question> mQuestions;
    MyConstants.Stage mStage;
    List<String> filterPrefStage;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> tagChoices = ((BaseActivity) getActivity()).getPreferences().getFilterTagChoices();
            List<String> stageChoices = ((BaseActivity) getActivity()).getPreferences().getFilterStageChoices();
            filterContentList(stageChoices, tagChoices);
        }
    };

    public QuestionListFragment() {
        super();
    }

    public static QuestionListFragment newInstance(MyConstants.Stage pStage) {
        QuestionListFragment fragment = new QuestionListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstants.Extras.KEY_STAGE, pStage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_question_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mStage = (MyConstants.Stage) bundle.getSerializable(MyConstants.Extras.KEY_STAGE);
        }
        initialize();
        setUpAdapter();
        if (savedInstanceState != null) {
            List<Question> questionList = (List<Question>) savedInstanceState.getSerializable(MyConstants.Extras.KEY_FILTERED_LIST);
            mQuestions = (List<Question>) savedInstanceState.getSerializable(MyConstants.Extras.KEY_LIST);
            mListAdapter.setDataCollection(questionList);
        } else
            loadQuestionList();

        filterPrefStage = new ArrayList<>();
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
                        loadQuestionList();
                    }
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).getPreferences().setFilterTagChoices(filterPrefStage);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(MyConstants.Extras.INTENT_FILTER));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MyConstants.Extras.KEY_FILTERED_LIST, (Serializable) mListAdapter.getDataCollection());
        outState.putSerializable(MyConstants.Extras.KEY_LIST, (Serializable) mQuestions);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        filterPrefStage = ((BaseActivity) getActivity()).getPreferences().getFilterTagChoices();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        mPresenter.pause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule(mStage))
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
        tvNoQuestion.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpAdapter() {
        mListAdapter = new ListAdapter<Question>(getContext(), new ArrayList<Question>(), (ListClickCallbackInterface) getActivity());
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void loadQuestionList() {
        mPresenter.initialize();
    }

    @Override
    public void renderQuestionList(List<Question> pQuestionList) {
        mQuestions = pQuestionList;
        mListAdapter.setDataCollection(pQuestionList);
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
        tvNoQuestion.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        tvNoQuestion.setVisibility(View.GONE);
    }

    public void filterContentList(List<String> pStageFilter, List<String> pTagFilter) {
        if (pStageFilter.isEmpty() && pTagFilter.isEmpty()) {
            mListAdapter.setDataCollection(mQuestions);
            ((QuestionListContainerFragment) getParentFragment()).changeFilterIcon(false);
            return;
        }

        ((QuestionListContainerFragment) getParentFragment()).changeFilterIcon(true);
        List<Question> filteredQuestion = new ArrayList<>();

        if (mQuestions != null) {
            for (Question question : mQuestions) {
                if ((question.getStage() != null && pStageFilter.contains(question.getStage()) ||
                        (question.getTags() != null && !Collections.disjoint(question.getTags(), pTagFilter)))) {
                    filteredQuestion.add(question);
                }
            }
        }
        mListAdapter.setDataCollection(filteredQuestion);
    }

}
