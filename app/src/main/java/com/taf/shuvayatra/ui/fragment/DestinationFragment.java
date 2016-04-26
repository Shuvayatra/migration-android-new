package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;

import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.DestinationFragmentPresenter;
import com.taf.shuvayatra.ui.activity.CountryDetailActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.DestinationView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class DestinationFragment extends BaseFragment implements DestinationView, ListItemClickListener, SearchView.OnQueryTextListener {

    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.searchView)
    SearchView mSearchView;
    List<Category> mCountries;
    List<Category> mSubCategories;

    @Inject
    DestinationFragmentPresenter mPresenter;
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
        mAdapter = new ListAdapter<Category>(getContext(),this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        DaggerDataComponent.builder().activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .dataModule(new DataModule(-1L, MyConstants.DataParent.COUNTRY))
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
    public void renderCountries(List<Category> pCountries) {
        separateCategories(pCountries);
        mAdapter.setDataCollection(mCountries);
    }


    private void separateCategories(List<Category> pCategories){
        for (Category category : pCategories) {
            if(category.getParentId() == null)
                mCountries.add(category);
            else
                mSubCategories.add(category);
        }
        Logger.e("JourneyFragment", "all subcatagories: "+mSubCategories);
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
    public void onListItemSelected(BaseModel pModel) {
        Category category  = ((Category) pModel);
        List<Category> subCategories = getSubCategoriesByParent(((Category) pModel).getCategoryId());
        Intent i = new Intent(getContext(), CountryDetailActivity.class);
        i.putExtra(MyConstants.Extras.KEY_CATEGORY, category);
        i.putExtra(MyConstants.Extras.KEY_SUBCATEGORY, (Serializable) subCategories);
        Logger.e("DestinationFragment", "subcatagories: "+subCategories);
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
                if (country.getName().toLowerCase().contains(query.toLowerCase()))
                    filterCountreis.add(country);
            }
        }
        mAdapter.setDataCollection(filterCountreis);
    }

    private List<Category> getSubCategoriesByParent(Long id){
        Logger.e("DestinationFragment", "parnet id" + id);
        List<Category> categories = new ArrayList<>();
        for (Category subCategory : mSubCategories) {
            if(subCategory.getParentId() == id){
                categories.add(subCategory);
            }
        }
        return categories;
    }
}
