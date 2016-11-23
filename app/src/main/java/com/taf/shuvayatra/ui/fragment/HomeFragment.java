package com.taf.shuvayatra.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.taf.data.utils.DateUtils;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.CountryWidgetData;
import com.taf.model.CountryWidgetModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryWidgetPresenter;
import com.taf.shuvayatra.presenter.HomePresenter;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.views.CountryWidgetView;
import com.taf.shuvayatra.ui.views.HomeView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements
        HomeView,
        CountryWidgetView,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "HomeFragment";

    @Inject
    HomePresenter mPresenter;
    @Inject
    CountryWidgetPresenter mCountryWidgetPresenter;
    CountryWidgetModel mCountryWidget;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;

    BlocksAdapter mAdapter;

    UseCaseData caseCalendar = new UseCaseData();
    UseCaseData caseForEx = new UseCaseData();
    UseCaseData caseWeather = new UseCaseData();


    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        mAdapter = new BlocksAdapter(getContext(), getChildFragmentManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeContainer.setOnRefreshListener(this);

        String selectedCountry = ((BaseActivity) getActivity()).getPreferences().getLocation();
        String countryName = selectedCountry.split(",")[Country.INDEX_TITLE_EN].substring(0, 1).toUpperCase() +
                selectedCountry.split(",")[Country.INDEX_TITLE_EN].substring(1,
                        selectedCountry.split(",")[Country.INDEX_TITLE_EN].length());
        Logger.e(TAG, "selectedCountry.split(): " + Arrays.toString(selectedCountry.split(",")));
        mCountryWidget = new CountryWidgetModel(countryName);
        mPresenter.initialize(null);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(null);
    }

    @Override
    public void renderBlocks(List<Block> data) {
        List<BaseModel> baseModels = new ArrayList<>();
        baseModels.addAll(data);
        String selectedCountry = ((BaseActivity) getActivity()).getPreferences().getLocation();
        if (!selectedCountry.equals(MyConstants.Preferences.DEFAULT_LOCATION)) {
            if (!data.isEmpty() && data.get(0).getLayout().equalsIgnoreCase("notice")) {
                baseModels.add(1, mCountryWidget);
            } else {
                baseModels.add(0, mCountryWidget);
            }
        }
        mAdapter.setBlocks(baseModels);
    }

    @Override
    public void showLoadingView() {
        mSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showErrorView(String errorMessage) {
        Snackbar.make(getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .dataModule(new DataModule())
                .activityModule(((BaseActivity) getActivity()).getActivityModule())
                .applicationComponent(((BaseActivity) getActivity()).getApplicationComponent())
                .build()
                .inject(this);
        mPresenter.attachView(this);
        mCountryWidgetPresenter.attachView(this);

        caseCalendar.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_CALENDAR);
        caseWeather.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_WEATHER);
        caseForEx.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_FOREX);

        // initialize each component for the country widget
        mCountryWidgetPresenter.initialize(caseCalendar);
        mCountryWidgetPresenter.initialize(caseWeather);
        mCountryWidgetPresenter.initialize(caseForEx);
    }

    @Override
    public void onComponentLoaded(CountryWidgetData.Component component) {
        try {
            switch (component.componentType()) {
                case CountryWidgetData.COMPONENT_CALENDAR:

                    mCountryWidget.setNepaliDate(((CountryWidgetData.CalendarComponent) component).getNepaliDate());
                    Calendar instance = ((CountryWidgetData.CalendarComponent) component).getToday();
                    String date = DateUtils.getFormattedDate(DateUtils.DEFAULT_DATE_PATTERN, instance.getTime());
                    String day = DateUtils.getEnglishDay(instance.get(Calendar.DAY_OF_WEEK));
                    String englishDate = day + ",\n" + date;
                    mCountryWidget.setEnglishDate(englishDate);
                    break;
                case CountryWidgetData.COMPONENT_FOREX:

                    if (!((BaseActivity) getActivity()).getPreferences().getLocation()
                            .equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION)) {

                        String country = ((BaseActivity) getActivity()).getPreferences().getLocation()
                                .split(",")[Country.INDEX_TITLE];
                        String foreignCurrency = ((CountryWidgetData.ForexComponent) component).getCurrencyMap().get(MyConstants
                                .Country.getCurrencyKey(country));

                        Logger.e(TAG, ">>> foreign currency: " + foreignCurrency);
                        mCountryWidget.setForex(String.format("%s 1 = NPR %s", MyConstants.Country.getCurrency(country), foreignCurrency));
                    } else {
                        // TODO: 11/10/16 send null
                        mCountryWidget.setForex("this is forex");
                    }
                    break;
                case CountryWidgetData.COMPONENT_WEATHER:

                    mCountryWidget.setTemperature(((CountryWidgetData.WeatherComponent) component).getTemperature() + " " + (char) 0x00B0 + "C");
                    String pWeather = ((CountryWidgetData.WeatherComponent) component).getWeatherInfo();
                    mCountryWidget.setWeather(pWeather);
                    if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_CLEAR_SKY)) {
                        Calendar cal = Calendar.getInstance();
                        if (cal.get(Calendar.HOUR_OF_DAY) < 19)
                            mCountryWidget.setImageResource(R.drawable.ic_clear_sky_day);
                        else
                            mCountryWidget.setImageResource(R.drawable.ic_clear_sky_night);
                    } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_BROKEN_CLOUDS) ||
                            pWeather.contains(MyConstants.WEATHER.TYPE_SCATTERED_CLOUDS)) {
                        mCountryWidget.setImageResource(R.drawable.ic_scattered_clouds);
                    } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_FEW_CLOUDS)) {
                        Calendar cal = Calendar.getInstance();
                        if (cal.get(Calendar.HOUR_OF_DAY) < 19)
                            mCountryWidget.setImageResource(R.drawable.ic_few_clouds_day);
                        else
                            mCountryWidget.setImageResource(R.drawable.ic_few_clouds_night);
                    } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_SHOWER_RAIN)) {
                        mCountryWidget.setImageResource(R.drawable.ic_shower_rain);
                    } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_THUNDERSTORM)) {
                        mCountryWidget.setImageResource(R.drawable.ic_thunderstorm);
                    } else if (pWeather.toLowerCase().contains(MyConstants.WEATHER.TYPE_RAIN)) {
                        mCountryWidget.setImageResource(R.drawable.ic_rain);
                    } else {
                        // TODO: 6/23/2016 unknown weather type
                    }
                    break;
            }
        } catch (NullPointerException e) {
            // TODO: 11/4/16 proper fix for context's NPE
            e.printStackTrace();
        }
    }


    @Override
    public void onLoadingView(int type) {

    }

    @Override
    public void onHideLoadingView(int type) {

    }

    @Override
    public void onErrorView(int type, String error) {

    }
}
