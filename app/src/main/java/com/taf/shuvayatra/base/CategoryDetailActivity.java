package com.taf.shuvayatra.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.taf.data.utils.Logger;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CategoryPresenter;
import com.taf.shuvayatra.ui.activity.CountryDetailActivity;
import com.taf.shuvayatra.ui.activity.InfoDetailActivity;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.shuvayatra.ui.interfaces.CategoryView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Nirazan-PC on 4/25/2016.
 */
public abstract class CategoryDetailActivity extends BaseActivity implements CategoryView {
    public Category mCategory;

    @Inject
    CategoryPresenter mPresenter;

    public abstract MyConstants.DataParent getDataParent();

    public void expandAppBar() {
    }

    public void addFeedFragment(List<Category> pCategories) {
        List<String> excludeTypes = null;
        if (!(this instanceof CountryDetailActivity) && !(this instanceof InfoDetailActivity)) {
            Logger.e("CategoryDetailActivity", "excluded: place");
            excludeTypes = new ArrayList<>();
            excludeTypes.add("place");
        }

        FeedFragment fragment = (FeedFragment) getSupportFragmentManager().findFragmentByTag
                ("feeds");
        if (fragment == null) {
            fragment = FeedFragment.newInstance(true, mCategory.getId(), mCategory.getTitle(),
                    excludeTypes,
                    pCategories
            );
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, "feeds")
                .commit();
    }

    void initialize() {
        DataModule dataModule = new DataModule(getDataParent(), false, mCategory.getId());
        DaggerDataComponent.builder().activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(dataModule)
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mPresenter.initialize(null);
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCategory = (Category) bundle.get(MyConstants.Extras.KEY_CATEGORY);
        }
        initialize();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void renderCategories(List<Category> pCategories) {
        addFeedFragment(pCategories);
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
    public Context getContext() {
        return this;
    }
}
