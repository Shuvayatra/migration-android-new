package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Category;
import com.taf.model.HeaderItem;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CategoryPresenter;
import com.taf.shuvayatra.ui.activity.InfoDetailActivity;
import com.taf.shuvayatra.ui.activity.TagListActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.CategoryView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class InfoFragment extends BaseFragment implements CategoryView, ListItemClickListener {


    @Inject
    CategoryPresenter mPresenter;
    @Bind(R.id.recyclerView)
    EmptyStateRecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    RelativeLayout mEmptyView;
    @Bind(R.id.search)
    EditText mSearchView;
    private ListAdapter mAdapter;

    public static InfoFragment getInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_info;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadCategories();
        setupAdapter();
    }

    void initialize() {
        DataModule dataModule = new DataModule(MyConstants.DataParent.INFO, true, null);
        DaggerDataComponent.builder().activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .dataModule(dataModule)
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    private void loadCategories() {
        mPresenter.initialize(null);
    }

    private void setupAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ListAdapter(getContext(), this, true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setEmptyMessage(getString(R.string.empty_section));
    }

    @Override
    public void renderCategories(List<Category> pCategories) {
        mAdapter.setDataCollection(addHeader(pCategories));
    }


    private List<BaseModel> addHeader(List<Category> pCategories) {
        List<BaseModel> headerCategories = new ArrayList<>();
        String sectionName = "";
        for (int i = 0; i < pCategories.size(); i++) {
            Category category = pCategories.get(i);
            Logger.e("InfoFragment", "header section :" + category.getParentAlias());
            if (!category.getParentAlias().equals(sectionName)) {
                sectionName = category.getParentAlias();
                Logger.e("InfoFragment", "header: " + sectionName);
                HeaderItem header = new HeaderItem(sectionName);
                header.setDataType(MyConstants.Adapter.TYPE_CATEGORY_HEADER);
                headerCategories.add(header);
            }
            headerCategories.add(category);

        }
        return headerCategories;
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
        Intent intent = new Intent(getContext(), InfoDetailActivity.class);
        intent.putExtra(MyConstants.Extras.KEY_CATEGORY, pModel);
        startActivity(intent);
    }

    @OnClick(R.id.search_action)
    public void showFilterTags() {
        Intent tagsIntent = new Intent(getActivity(), TagListActivity.class);
        startActivity(tagsIntent);
    }
}
