package com.taf.shuvayatra.ui.fragment;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.taf.interactor.UseCaseData;
import com.taf.model.Info;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.databinding.InfoDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.InfoPresenter;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.views.InfoView;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import static com.taf.util.MyConstants.Extras.KEY_CONTACT_US;
import static com.taf.util.MyConstants.Extras.KEY_INFO;

/**
 * Created by yipl on 1/16/17.
 */

public class InfoFragment extends BaseFragment implements InfoView {

    private static final String TAG = "InfoFragment";

    private String mKey;

    private Info mInfo;

    @Inject
    InfoPresenter mPresenter;

    InfoDataBinding mBinding;

    @Override
    public int getLayout() {
        return R.layout.fragment_about;
    }

    public static InfoFragment newInstance(String key) {
        InfoFragment infoFragment = new InfoFragment();
        infoFragment.mKey = key;
        return infoFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            mKey = (String) savedInstanceState.get("KEY");
        }
            switch (mKey) {
                case MyConstants.Extras.KEY_ABOUT:
                    ((BaseActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.about));
                    break;
                case KEY_CONTACT_US:
                    ((BaseActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.contact));
                    break;
            }
        initialize();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .dataModule(new DataModule())
                .build()
                .inject(this);

        mPresenter.attachView(this);

        UseCaseData pData = new UseCaseData();
        pData.putString(KEY_INFO, mKey);

        mPresenter.initialize(pData);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(getView(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void render(Info info) {
        Log.e(TAG, "render: " + info);
        mInfo = info;
        mBinding.setInfo(mInfo);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_INFO, mInfo);
        outState.putString("KEY", mKey);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mKey = (String) savedInstanceState.get("KEY");
            mInfo = (Info) savedInstanceState.getSerializable(KEY_INFO);
            mBinding.setInfo(mInfo);
        }
    }
}
