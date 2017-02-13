package com.taf.shuvayatra.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.taf.data.utils.DateUtils;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.CountryWidgetData;
import com.taf.model.CountryWidgetModel;
import com.taf.model.Notice;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseNavigationFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryWidgetPresenter;
import com.taf.shuvayatra.presenter.HomePresenter;
import com.taf.shuvayatra.ui.activity.HomeActivity;
import com.taf.shuvayatra.ui.adapter.BlocksAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.BlockItemAnalytics;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.interfaces.ListItemClickWithDataTypeListener;
import com.taf.shuvayatra.ui.views.CountryWidgetView;
import com.taf.shuvayatra.ui.views.HomeView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.shuvayatra.util.Utils;
import com.taf.util.MyConstants;
import com.taf.util.MyConstants.Adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;
import static android.text.format.DateUtils.FORMAT_SHOW_YEAR;
import static android.text.format.DateUtils.formatDateTime;

public class HomeFragment extends BaseNavigationFragment implements
        HomeView,
        CountryWidgetView,
        SwipeRefreshLayout.OnRefreshListener,
        ListItemClickWithDataTypeListener,
        BlockItemAnalytics {

    public static final String TAG = "HomeFragment";

    @Inject
    HomePresenter mPresenter;
    @Inject
    CountryWidgetPresenter mCountryWidgetPresenter;
    CountryWidgetModel mCountryWidget;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    @BindView(R.id.empty_view)
    View emptyView;

    BlocksAdapter mAdapter;

    UseCaseData caseCalendar = new UseCaseData();
    UseCaseData caseForEx = new UseCaseData();
    UseCaseData caseWeather = new UseCaseData();

    private boolean showCountryWidget;

    @Override
    public String screenName() {
        return "Navigation - Home";
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public Fragment defaultInstance() {
        return newInstance();
    }

    @Override
    public String fragmentTag() {
        return TAG;
    }

    @Override
    public RecyclerView fragmentRecycler() {
        return mRecyclerView;
    }

    @Override
    public RecyclerView.ItemDecoration initDecorator() {
        return Utils.getBottomMarginDecoration(getContext(), R.dimen.mini_media_player_peek_height);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initNavigation(int layout, FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG) == null ?
                HomeFragment.newInstance() :
                fragmentManager.findFragmentByTag(TAG);
        fragmentManager.beginTransaction().replace(layout, fragment, TAG).commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String selectedCountry = ((BaseActivity) getActivity()).getPreferences().getLocation();
        showCountryWidget = !(selectedCountry.equalsIgnoreCase(getString(R.string.country_not_decided_yet)) ||
                selectedCountry.equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION));

        if (showCountryWidget) {
            Country country = Country.makeCountryFromPreference(((BaseActivity) getActivity())
                    .getPreferences().getLocation());
            mCountryWidget = new CountryWidgetModel(country.getTitle());
            mCountryWidget.setId(country.getId());
            mCountryWidget.setTimeZoneId(country.getTimeZoneId());
        }

        initialize();

        mAdapter = new BlocksAdapter(getContext(), this, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(emptyView);
        mSwipeContainer.setOnRefreshListener(this);

        mPresenter.initialize(getUserCredentialsUseCase());
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onListItemSelected(BaseModel model, int dataType, int adapterPosition) {

        if (dataType == Adapter.VIEW_TYPE_RADIO_WIDGET) {
            // send to radio widget
            getContext().sendBroadcast(new Intent(MyConstants.Intent.ACTION_SHOW_RADIO));
        }

        if (dataType == MyConstants.Adapter.VIEW_TYPE_NOTICE) {
            // dismiss notice
            mAdapter.getBlocks().remove(adapterPosition);
            mAdapter.notifyItemRemoved(adapterPosition);
            getTypedActivity().getPreferences().setNoticeDismissId(((Block) model).getNotice().getId());
        }

        if (dataType == Adapter.TYPE_COUNTRY_WIDGET) {
            // send to destination detail
            Logger.e(TAG, ">>> CLICK ON COUNTRY WIDGET");
            Intent intent = new Intent(MyConstants.Intent.ACTION_SHOW_DESTINATION);
            intent.putExtra(MyConstants.Extras.KEY_COUNTRY_WIDGET, model);
            getContext().sendBroadcast(intent);
        }
    }

    @Override
    public void onDeeplinkSelected(String deeplink, BaseModel model, int dataType, int adapterPosition) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
        if (dataType == MyConstants.Adapter.VIEW_TYPE_NOTICE) {
            Notice notice = ((Block) model).getNotice();
            intent.putExtra(MyConstants.Extras.KEY_PAGE_TITLE, notice.getTitle());
            intent.putExtra(MyConstants.Extras.KEY_ID, notice.getId());

            AnalyticsUtil.logReadMoreEvent(getAnalytics(), model.getId(), ((Block) model).getNotice()
                    .getTitle(), screenName(), ((Block) model).getLayout());
        } else {
            intent.putExtra(MyConstants.Extras.KEY_PAGE_TITLE, ((Block) model).getTitle());
            intent.putExtra(MyConstants.Extras.KEY_POST_ID, model.getId());

            AnalyticsUtil.logReadMoreEvent(getAnalytics(), model.getId(), ((Block) model).getTitle(),
                    screenName(), ((Block) model).getLayout());
        }
        startActivity(intent);
    }

    @Override
    public void onBlockItemSelected(Block block, Post post) {
        AnalyticsUtil.logBlockEvent(getAnalytics(), block.getTitle(), post.getTitle(),
                screenName(), block.getLayout());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onRefresh() {
        mPresenter.initialize(getUserCredentialsUseCase());
        if (showCountryWidget) initCountryWidgetPresenter();
        ((HomeActivity) getActivity()).startScreenRequest();
    }

    @Override
    public void renderBlocks(List<Block> data) {
        List<BaseModel> baseModels = new ArrayList<>();
        baseModels.addAll(data);
        String selectedCountry = ((BaseActivity) getActivity()).getPreferences().getLocation();
        if (!selectedCountry.equals(MyConstants.Preferences.DEFAULT_LOCATION) && showCountryWidget) {
            baseModels.add(mCountryWidget);
        }
        mAdapter.setBlocks(Utils.sortBlock(baseModels));
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

        if (showCountryWidget) initCountryWidgetPresenter();
    }

    public void initCountryWidgetPresenter() {
        Country country = Country.makeCountryFromPreference(((BaseActivity) getActivity())
                .getPreferences().getLocation());

        caseCalendar.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_CALENDAR);
        caseCalendar.putSerializable(UseCaseData.CALENDAR_INSTANCE,
                DateUtils.getTimeZoneCalendarInstance(country.getTimeZoneId()));

        caseWeather.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_WEATHER);
        caseWeather.putString(UseCaseData.COUNTRY_CODE, country.getTitleEnglish() + "," + country.getTitle());

        caseForEx.putInteger(UseCaseData.COMPONENT_TYPE, CountryWidgetData.COMPONENT_FOREX);

        // initialize each component for the country widget
        mCountryWidgetPresenter.initialize(caseCalendar);
        mCountryWidgetPresenter.initialize(caseWeather);
        mCountryWidgetPresenter.initialize(caseForEx);
    }

    @Override
    public void onComponentLoaded(CountryWidgetData.Component component) {
        try {

            Country country = Country.makeCountryFromPreference(((BaseActivity) getActivity())
                    .getPreferences().getLocation());

            switch (component.componentType()) {
                case CountryWidgetData.COMPONENT_CALENDAR:

                    mCountryWidget.setNepaliDate(((CountryWidgetData.CalendarComponent) component)
                            .getNepaliDate());
                    mCountryWidget.setEnglishDate(String.format("%s,\n%s", formatDateTime(getContext(),
                            ((CountryWidgetData.CalendarComponent) component).getToday().getTimeInMillis(),
                            FORMAT_SHOW_WEEKDAY), formatDateTime(getContext(),
                            ((CountryWidgetData.CalendarComponent) component).getToday().getTimeInMillis(),
                            FORMAT_SHOW_YEAR)));
                    break;
                case CountryWidgetData.COMPONENT_FOREX:

                    if (!((BaseActivity) getActivity()).getPreferences().getLocation()
                            .equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION)) {

                        String foreignCurrency = ((CountryWidgetData.ForexComponent) component)
                                .getCurrencyMap().get(MyConstants.Country.getCurrencyKey(country.getTitle()));

                        if (foreignCurrency != null)
                            mCountryWidget.setForex(String.format("%s 1 = NPR %s",
                                    MyConstants.Country.getCurrency(country.getTitle()),
                                    foreignCurrency));
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
            // make the change in mScreenModel reflect in widget
            if (mAdapter != null && mAdapter.getBlocks() != null && mAdapter.getBlocks()
                    .contains(mCountryWidget)) {
                mAdapter.notifyItemChanged(mAdapter.getBlocks().indexOf(mCountryWidget));
            }
        } catch (NullPointerException e) {
            // TODO: 11/4/16 proper fix for context's NPE
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadingView(int type) {
        mSwipeContainer.setRefreshing(true);
    }

    @Override
    public void onHideLoadingView(int type) {
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void onErrorView(int type, String error) {
        Snackbar.make(mSwipeContainer, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();
    }


}
