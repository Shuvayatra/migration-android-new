package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.taf.shuvayatra.ui.activity.JourneyCategoryDetailActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.custom.MarginItemDecoration;
import com.taf.shuvayatra.ui.interfaces.CategoryView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class JourneyFragment extends BaseFragment implements CategoryView, ListItemClickListener, SearchView.OnQueryTextListener {

    @Inject
    CategoryPresenter mPresenter;
    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    @Bind(R.id.searchView)
    SearchView mSearchView;

    private ListAdapter mAdapter;
    private List<Category> mCategories;
    private List<Category> mSubCategories;

    @Override
    public int getLayout() {
        return R.layout.fragment_journey;
    }

    public static JourneyFragment newInstance(){
        JourneyFragment fragment = new JourneyFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadCategories();
        setupAdapter();
    }

    private void setupAdapter() {
        mAdapter = new ListAdapter(getContext(), this);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new MarginItemDecoration(getContext(), R.dimen.spacing_xxsmall));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setEmptyMessage(getString(R.string.empty_section));
    }

    void initialize(){
        DataModule dataModule = new DataModule(MyConstants.DataParent.JOURNEY, true);
        DaggerDataComponent.builder().activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .dataModule(dataModule)
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mCategories = new ArrayList<>();
        mSubCategories = new ArrayList<>();
        mSearchView.setOnQueryTextListener(this);
    }

    private void loadCategories(){
        mPresenter.initialize(null);
    }


    @Override
    public void renderCategories(List<Category> pCategories) {
        mAdapter.setDataCollection(pCategories);
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
        Intent i = new Intent(getContext(), JourneyCategoryDetailActivity.class);
        i.putExtra(MyConstants.Extras.KEY_CATEGORY, category);
        startActivity(i);
    }

    @Override
    public void onListItemSelected(List<BaseModel> pCollection, int pIndex) {

    }

    void filterCountries(String query){
        List<Category> filterCountreis = new ArrayList<>();
        if(mCategories!=null) {
            for (Category country : mCategories) {
                if (country.getTitle().toLowerCase().contains(query.toLowerCase()))
                    filterCountreis.add(country);
            }
        }
        mAdapter.setDataCollection(filterCountreis);
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
}
