package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.widget.RelativeLayout;

import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.JourneyFragmentPresenter;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.custom.MarginItemDecoration;
import com.taf.shuvayatra.ui.interfaces.JourneyView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class JourneyFragment extends BaseFragment implements JourneyView, ListItemClickListener {

    @Inject
    JourneyFragmentPresenter mPresenter;
    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;

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
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new MarginItemDecoration(getContext(),R.dimen.spacing_xxsmall));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setEmptyMessage(getString(R.string.empty_section));
    }

    void initialize(){
        DaggerDataComponent.builder().activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .dataModule(new DataModule())
                .build()
                .inject(this);
        UseCaseData data = new UseCaseData();
        data.putString(UseCaseData.SECTION_NAME, MyConstants.SECTION.JOURNEY);
        mPresenter.attachView(this);
        mCategories = new ArrayList<>();
        mSubCategories = new ArrayList<>();
    }

    private void loadCategories(){
        UseCaseData data = new UseCaseData();
        data.putString(UseCaseData.SECTION_NAME,MyConstants.SECTION.JOURNEY);
        mPresenter.initialize(data);
    }


    @Override
    public void renderCategories(List<Category> pCategories) {
        separateCategories(pCategories);
        mAdapter.setDataCollection(mCategories);
    }

    private void separateCategories(List<Category> pCategories){
        for (Category category : pCategories) {
            if(category.getParentId() == null)
                mCategories.add(category);
            else
                mSubCategories.add(category);
        }
    }

    private List<Category> getSubCategoriesByParent(Long id){
        List<Category> categories = new ArrayList<>();
        for (Category subCategory : mSubCategories) {
            if(subCategory.getParentId() == id){
                categories.add(subCategory);
            }
        }
        return categories;
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
        getSubCategoriesByParent(((Category) pModel).getCategoryId());
    }

    @Override
    public void onListItemSelected(List<BaseModel> pCollection, int pIndex) {

    }
}
