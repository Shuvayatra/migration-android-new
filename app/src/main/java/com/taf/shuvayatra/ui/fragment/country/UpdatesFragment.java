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
import com.yipl.nrna.domain.model.CountryUpdate;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.UpdatesPresenter;
import com.yipl.nrna.ui.adapter.ListAdapter;
import com.yipl.nrna.ui.interfaces.UpdateListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nirazan-PC on 12/14/2015.
 */
public class UpdatesFragment extends BaseFragment implements
        UpdateListView {

    protected ListAdapter<CountryUpdate> mListAdapter;
    protected List<CountryUpdate> mUpdates;

    @Inject
    UpdatesPresenter mPresenter;

    @Bind(R.id.updates_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.no_updates)
    TextView tvNoUpdates;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    RelativeLayout mContainer;
    Long mCountryId = Long.MIN_VALUE;

    public UpdatesFragment() {
        super();
    }

    public static UpdatesFragment newInstance(Long pId) {
        UpdatesFragment fragment = new UpdatesFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(MyConstants.Extras.KEY_ID, pId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_updates;
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
            mCountryId = bundle.getLong(MyConstants.Extras.KEY_ID);
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
        mListAdapter = new ListAdapter<>(getContext(), null);
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void initialize() {
        if (mCountryId != Long.MIN_VALUE) {
            DaggerDataComponent.builder()
                    .dataModule(new DataModule(mCountryId))
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
        tvNoUpdates.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void renderUpdates(List<CountryUpdate> pUpdates) {
        mUpdates = pUpdates;
        mListAdapter.setDataCollection(mUpdates);
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
        tvNoUpdates.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        tvNoUpdates.setVisibility(View.INVISIBLE);
    }

}
