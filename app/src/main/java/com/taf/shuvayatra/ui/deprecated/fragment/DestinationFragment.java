package com.taf.shuvayatra.ui.deprecated.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RelativeLayout;

import com.taf.model.BaseModel;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.deprecated.CategoryPresenter;
import com.taf.shuvayatra.ui.deprecated.activity.CountryDetailActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.deprecated.interfaces.CategoryView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DestinationFragment extends BaseFragment implements
        CategoryView,
        ListItemClickListener {

    @BindView(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;
    List<Category> mCountries;
    List<Category> mSubCategories;

    @Inject
    CategoryPresenter mPresenter;
    private ListAdapter<Category> mAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_destination_old;
    }

    public static DestinationFragment newInstance() {
        DestinationFragment fragment = new DestinationFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadCountries();
        setUpAdapter();
    }

    private void setUpAdapter() {
        mAdapter = new ListAdapter<>(getContext(),this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setEmptyMessage(getString(R.string.empty_section));
    }

    private void initialize() {
        DaggerDataComponent.builder().activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .dataModule(new DataModule(MyConstants.DataParent.COUNTRY, true, null))
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mCountries = new ArrayList<>();
        mSubCategories = new ArrayList<>();
    }

    void loadCountries() {
        mPresenter.initialize(null);
    }

    @Override
    public void renderCategories(List<Category> pCountries) {
        mCountries = pCountries;
        mAdapter.setDataCollection(pCountries);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showErrorView(String pErrorMessage) {

    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        Category category = ((Category) pModel);
        Intent i = new Intent(getContext(), CountryDetailActivity.class);
        i.putExtra(MyConstants.Extras.KEY_CATEGORY, category);
        startActivity(i);
    }
}
