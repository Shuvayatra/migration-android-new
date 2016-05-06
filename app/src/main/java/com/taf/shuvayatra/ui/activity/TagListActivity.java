package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.TagListDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.TagListPresenter;
import com.taf.shuvayatra.ui.interfaces.TagListView;
import com.taf.util.MyConstants;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class TagListActivity extends BaseActivity implements
        TagListView {

    @Inject
    TagListPresenter mPresenter;

    @Bind(R.id.tag_list_container)
    FlowLayout mTagsContainer;

    @Override
    public int getLayout() {
        return R.layout.activity_tag_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();
        mPresenter.initialize(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule())
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void renderTagList(List<String> pTags) {
        mTagsContainer.removeAllViews();
        for (final String tag : pTags) {
            TagListDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.view_tag, mTagsContainer, false);
            dataBinding.setTag(tag);
            dataBinding.setSelected(false);
            View view = dataBinding.getRoot();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();
                    data.putExtra(MyConstants.Extras.KEY_TAG, tag);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
            mTagsContainer.addView(view);
        }
    }

    @Override
    public void showLoadingView() {
    }

    @Override
    public void hideLoadingView() {
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mTagsContainer, pErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
