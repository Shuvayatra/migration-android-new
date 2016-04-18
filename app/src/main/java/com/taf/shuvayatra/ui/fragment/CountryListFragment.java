package com.taf.shuvayatra.ui.fragment;

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
import com.yipl.nrna.domain.model.Country;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.presenter.CountryListFragmentPresenter;
import com.yipl.nrna.ui.activity.MainActivity;
import com.yipl.nrna.ui.adapter.ListAdapter;
import com.yipl.nrna.ui.interfaces.CountryListView;
import com.yipl.nrna.ui.interfaces.ListClickCallbackInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nirazan-PC on 12/11/2015.
 */
public class CountryListFragment extends BaseFragment implements CountryListView {

    @Inject
    CountryListFragmentPresenter mPresenter;

    @Bind(R.id.countryList)
    RecyclerView mRecyclerView;
    @Bind(R.id.tvNoCountry)
    TextView tvNoCountry;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.data_container)
    RelativeLayout mContainer;

    private ListAdapter<Country> mListAdapter;
    private List<Country> mCountries;

    public CountryListFragment() {
        super();
    }

    public static CountryListFragment newInstance() {
        CountryListFragment fragment = new CountryListFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_country_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setUpAdapter();
        loadCountryList();
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
                        loadCountryList();
                    }
                })
                .show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void loadCountryList() {
        mPresenter.initialize();
    }

    private void setUpAdapter() {
        mListAdapter = new ListAdapter<>(getContext(), (ListClickCallbackInterface) getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(((MainActivity) getActivity()).getActivityModule())
                .applicationComponent(((MainActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void showLoadingView() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mProgressBar.setVisibility(View.GONE);
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
        tvNoCountry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        tvNoCountry.setVisibility(View.INVISIBLE);
    }

    @Override
    public void renderCountryList(List<Country> pCountries) {
        List<Country> preferedCountries = new ArrayList<>();
        if (pCountries != null) {
            mCountries = pCountries;
            List<String> countriesName = ((BaseActivity) getActivity()).getPreferences().getCountries();
            if (countriesName != null && !countriesName.isEmpty()) {
                for (String s : countriesName) {
                    for (Country country : pCountries) {
                        if (s.equals(country.getName())) {
                            preferedCountries.add(country);
                            break;
                        }
                    }
                }

                Country footer = new Country();
                footer.setDataType(MyConstants.Adapter.TYPE_FOOTER);
                preferedCountries.add(footer);
                mListAdapter.setDataCollection(preferedCountries);
                return;
            }
        }
        mListAdapter.setDataCollection(pCountries);
    }

    public void showAllCountries() {
        mListAdapter.setDataCollection(mCountries);
    }

}
