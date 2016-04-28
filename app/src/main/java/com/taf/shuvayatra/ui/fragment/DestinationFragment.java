package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;

import com.taf.model.BaseModel;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CategoryPresenter;
import com.taf.shuvayatra.ui.activity.CountryDetailActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.CategoryView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class DestinationFragment extends BaseFragment implements CategoryView, ListItemClickListener, SearchView.OnQueryTextListener {

    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    @Bind(R.id.searchView)
    SearchView mSearchView;
    List<Category> mCountries;
    List<Category> mSubCategories;

    @Inject
    CategoryPresenter mPresenter;
    private ListAdapter<Category> mAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_destination;
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
        mSearchView.setOnQueryTextListener(this);
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
                .dataModule(new DataModule(MyConstants.DataParent.COUNTRY, true))
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mCountries = new ArrayList<>();
        mSubCategories = new ArrayList<>();
    }

    void loadCountries(){
        mPresenter.initialize(null);
    }

    @Override
    public void renderCategories(List<Category> pCountries) {
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
        Category category  = ((Category) pModel);
        Intent i = new Intent(getContext(), CountryDetailActivity.class);
        i.putExtra(MyConstants.Extras.KEY_CATEGORY, category);
        startActivity(i);
    }

    @Override
    public void onListItemSelected(List<BaseModel> pCollection, int pIndex) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filterCountries(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        filterCountries(query);
        return true;
    }

    void filterCountries(String query){
        List<Category> filterCountreis = new ArrayList<>();
        if(mCountries!=null) {
            for (Category country : mCountries) {
                if (country.getTitle().toLowerCase().contains(query.toLowerCase()))
                    filterCountreis.add(country);
            }
        }
        mAdapter.setDataCollection(filterCountreis);
    }

}
