package com.taf.shuvayatra.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Country;
import com.taf.model.CountryWidgetModel;
import com.taf.model.ScreenModel;
import com.taf.model.UserInfoModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.base.MediaServiceActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.CountryListPresenter;
import com.taf.shuvayatra.presenter.HomeActivityPresenter;
import com.taf.shuvayatra.presenter.OnBoardingPresenter;
import com.taf.shuvayatra.ui.deeplink.HomeDeeplink;
import com.taf.shuvayatra.ui.deeplink.ScreenDeeplink;
import com.taf.shuvayatra.ui.fragment.BlockScreenFragment;
import com.taf.shuvayatra.ui.fragment.ChannelFragment;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.FeedScreenFragment;
import com.taf.shuvayatra.ui.fragment.HomeFragment;
import com.taf.shuvayatra.ui.fragment.UserAccountFragment;
import com.taf.shuvayatra.ui.interfaces.HomeDeeplinkInteraction;
import com.taf.shuvayatra.ui.interfaces.IDeeplinkHandler;
import com.taf.shuvayatra.ui.interfaces.IDynamicNavigationFragment;
import com.taf.shuvayatra.ui.interfaces.INavigationFragment;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.shuvayatra.ui.views.HomeActivityView;
import com.taf.shuvayatra.ui.views.OnBoardingView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.util.MyConstants;
import com.taf.util.MyConstants.Deeplink;
import com.taf.util.MyConstants.DynamicScreen;
import com.taf.util.MyConstants.Extras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.taf.util.MyConstants.Extras.KEY_ABOUT;
import static com.taf.util.MyConstants.Extras.KEY_CONTACT_US;
import static com.taf.util.MyConstants.Extras.KEY_INFO;

public class HomeActivity extends MediaServiceActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeActivityView,
        OnBoardingView, HomeDeeplinkInteraction, CountryView {

    public static final String TAG = "HomeActivity";
    public static final String SCREEN_NAME = "Home Screen";
    public static final String EVENT_MENU_NAVIGATION = "menu_navigation";
    private static final String STATE_MENU_ID = "nav-selected-menu-id";

    // offset to add menu ( i.e after home, destination
    public static final int MENU_OFFSET = 2;
    private static final String STATE_SCREENS = "screens";

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.searchbox_container)
    LinearLayout mSearchBox;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @Inject
    OnBoardingPresenter onBoardingPresenter;
    @Inject
    HomeActivityPresenter homeActivityPresenter;
    @Inject
    CountryListPresenter countryListPresenter;

    private Map<Integer, INavigationFragment> staticNavigationMap = new HashMap<>();
    private Map<Integer, IDynamicNavigationFragment> dynamicNavigationMap = new HashMap<>();
    private Map<ScreenModel, Boolean> dynamicScreenStatus = new HashMap<>();
    private List<ScreenModel> mScreens;
    private List<Country> countryList;
    private int selectedNavMenuId;

    @Override
    public String screenName() {
        return SCREEN_NAME;
    }

    private BroadcastReceiver mRadioCallbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(MyConstants.Intent.ACTION_SHOW_RADIO)) {
                showFragment(R.id.nav_radio);
            } else if (intent.getAction().equalsIgnoreCase(MyConstants.Intent.ACTION_SHOW_DESTINATION)) {

                CountryWidgetModel widget = (CountryWidgetModel) intent
                        .getSerializableExtra(Extras.KEY_COUNTRY_WIDGET);
                int index = countryList.indexOf(widget);

                if (index != -1) {

                    Intent destinationIntent = new Intent(HomeActivity.this,
                            DestinationDetailActivity.class);
                    destinationIntent.putExtra(MyConstants.Extras.KEY_COUNTRY, countryList.get(index));
                    startActivity(destinationIntent);
                }
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(mRadioCallbackReceiver);
    }

    public void startScreenRequest() {
        if (homeActivityPresenter != null)
            homeActivityPresenter.initialize(getUserCredentialsUseCase());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, getToolbar(), R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mScreens = new ArrayList<>();
        initialize();

        if (savedInstanceState != null) {
            selectedNavMenuId = savedInstanceState.getInt(STATE_MENU_ID);
            mScreens = (List<ScreenModel>) savedInstanceState.get(STATE_SCREENS);
            countryList = (List<Country>) savedInstanceState.get(Extras.KEY_COUNTRY_LIST);
            Logger.e(TAG, ">>> recreating screens: " + mScreens.size());
            processMenu(mScreens);
        } else {
            selectedNavMenuId = R.id.nav_home;
            startScreenRequest();
            fetchCountries();
        }

        showFragment(selectedNavMenuId);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSearchBox
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                });

        // send onboarding data
        if (getPreferences().isUserOnBoardingComplete() && !getPreferences().isUserInfoSynced()) {
            sendUserInfo();
        }


    }

    private void initialize() {
        DaggerDataComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .dataModule(new DataModule())
                .build()
                .inject(this);

        homeActivityPresenter.attachView(this);
        onBoardingPresenter.attachView(this);
        countryListPresenter.attachView(this);
    }

    private void fetchCountries() {
        // gather country cache
        UseCaseData data = new UseCaseData();
        data.putBoolean(UseCaseData.CACHED_DATA, true);
        countryListPresenter.initialize(data);
    }

    private void sendUserInfo() {
        Logger.e(TAG, "sending userInfo");
        UseCaseData useCaseData = new UseCaseData();
        useCaseData.putSerializable(UseCaseData.USER_INFO, getUserInfo());
        onBoardingPresenter.initialize(useCaseData);
    }

    public UserInfoModel getUserInfo() {
        UserInfoModel userInfo = new UserInfoModel();
        userInfo.setName(getPreferences().getUserName());
        userInfo.setBirthday(getPreferences().getBirthday());
        String countryInfo = getPreferences().getLocation();
        if (!countryInfo.equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION) &&
                !countryInfo.equalsIgnoreCase(getString(R.string.country_not_decided_yet))) {
            if (countryInfo.split(",").length > 2) {
                userInfo.setDestinedCountry(TextUtils.split(countryInfo, ",")[2]);
            } else {
                userInfo.setDestinedCountry(TextUtils.split(countryInfo, ",")[1]);
            }
        } else {
            userInfo.setDestinedCountry(null);
        }
        Locale locale = Locale.getDefault();
        userInfo.setWorkStatus(getPreferences().getPreviousWorkStatus());
        int id = getPreferences().getOriginalLocation();

        String[] zones = getResources().getStringArray(R.array.zones);
        userInfo.setOriginalLocation(zones[id]);
        String gender = getPreferences().getGender();
        if (gender.equalsIgnoreCase(getString(R.string.gender_male))) {
            userInfo.setGender("M");
        } else if (gender.equalsIgnoreCase(getString(R.string.gender_female))) {
            userInfo.setGender("F");
        } else if (gender.equalsIgnoreCase(getString(R.string.gender_other))) {
            userInfo.setGender("O");
        }
        Logger.e(TAG, "userInfo: " + userInfo);
        return userInfo;
    }

    protected void onResume() {
        super.onResume();
        Logger.e(TAG, ">>>>> ON RESUME");
        IntentFilter filter = new IntentFilter(MyConstants.Intent.ACTION_SHOW_RADIO);
        filter.addAction(MyConstants.Intent.ACTION_SHOW_DESTINATION);
        registerReceiver(mRadioCallbackReceiver, filter);

        if (selectedNavMenuId == R.id.nav_account) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(0);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(getResources().getDimensionPixelOffset(R.dimen.spacing_xsmall));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        if (fragment == null) {
            mNavigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, HomeFragment.newInstance(), HomeFragment.TAG)
                    .commit();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        showFragment(id);
        return true;
    }

    @Override
    public void renderCountries(List<Country> countryList) {
        this.countryList = countryList;
    }

    @Override
    public void onIdentifierRetrieved(int id) {
        if (selectedNavMenuId == id) {
            // just refresh home page
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
            if (fragment != null) {
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onRefresh();
                    getIntent().putExtra(Deeplink.KEY_DEEPLINK_LOADED, true);
                } else {
                    // notify error ?
                }
            }
        } else {
            selectedNavMenuId = R.id.nav_home;
            showFragment(selectedNavMenuId);
        }
    }

    @Override
    public void onRedirectScreen(ScreenModel screenModel, int listIndex) {
        if (screenModel.getId() == selectedNavMenuId) {
            // just refresh screen
            ScreenModel model = new ScreenModel();
            model.setId(screenModel.getId());
            if (listIndex != -1) {

                ScreenModel screen = mScreens.get(listIndex);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(screen
                        .getType().equalsIgnoreCase(DynamicScreen.TYPE_FEED) ? FeedScreenFragment.TAG :
                        BlockScreenFragment.TAG + screen.getId());

                if (fragment != null) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).onRefresh();
                        getIntent().putExtra(Deeplink.KEY_DEEPLINK_LOADED, true);
                    }
                } else {
                    // notify users of error
                }
            }
        } else {
            if (listIndex != -1) {
                ScreenModel screen = mScreens.get(listIndex);
                MenuItem menuItem = mNavigationView.getMenu().findItem(screenModel.getId().intValue());
                selectDynamicScreen(menuItem, screen);
                getIntent().putExtra(Deeplink.KEY_DEEPLINK_LOADED, true);
            } else {
                // sorry could not find screen message
                Snackbar.make(mDrawer, "Error could not find screen", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void showFragment(int menuId) {

        if (staticNavigationMap.isEmpty()) {
            staticNavigationMap.put(R.id.nav_home, HomeFragment.newInstance());
            staticNavigationMap.put(R.id.nav_radio, ChannelFragment.newInstance());
            staticNavigationMap.put(R.id.nav_destination, DestinationFragment.newInstance());
            staticNavigationMap.put(R.id.nav_account, UserAccountFragment.newInstance());
            staticNavigationMap.put(R.id.nav_about, new INavigationFragment() {
                @Override
                public void initNavigation(int layout, FragmentManager fragmentManager) {
                    // don't use this parameter here
                    Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                    intent.putExtra(KEY_INFO, KEY_ABOUT);
                    startActivity(intent);
                }

                @Override
                public String screenName() {
                    return "Navigation - About";
                }
            });
            staticNavigationMap.put(R.id.nav_contact, new INavigationFragment() {
                @Override
                public void initNavigation(int layout, FragmentManager fragmentManager) {
                    // don't use this parameter here
                    Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                    intent.putExtra(KEY_INFO, KEY_CONTACT_US);
                    startActivity(intent);
                }

                @Override
                public String screenName() {
                    return "Navigation - Contact";
                }
            });
        }

        if (staticNavigationMap.containsKey(menuId)) {
            staticNavigationMap.get(menuId).initNavigation(R.id.content_home, getSupportFragmentManager());
            updateNavigationAnalytics(menuId, staticNavigationMap.get(menuId).screenName());
            updateNavigationViewAndReferences(menuId);
        }
    }

    private void updateNavigationAnalytics(long menuId, String screenName) {
        AnalyticsUtil.logViewEvent(getAnalytics(), menuId, screenName, SCREEN_NAME);
    }

    private void updateNavigationViewAndReferences(int menuId) {

        // update navigation view
        mNavigationView.setCheckedItem(menuId);
        selectedNavMenuId = menuId;

        // have toolbar's elevate 0 or 1
        if (menuId == R.id.nav_account) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(0);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(getResources().getDimensionPixelOffset(R.dimen.spacing_xsmall));
            }
        }

        mDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_MENU_ID, selectedNavMenuId);
        outState.putSerializable(STATE_SCREENS, (Serializable) mScreens);
        outState.putSerializable(Extras.KEY_COUNTRY_LIST, (Serializable) countryList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showErrorView(String pErrorMessage) {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }


    @Override
    public void renderScreens(List<ScreenModel> screens) {
        mScreens = screens;
        // check for potential intents resulting from deeplinks
        if (isFromDeeplink()) {
            for (ScreenModel screen : screens) {
                // collect all the dynamic screens and check if they have been loaded
                dynamicScreenStatus.put(screen, false);
            }
        }
        processMenu(screens);
    }

    private boolean isFromDeeplink() {
        return getIntent() != null && getIntent().getData() != null
                && !getIntent().getBooleanExtra(Deeplink.KEY_DEEPLINK_LOADED, false);
    }

    private void navigateToDeeplink() {
        if (isFromDeeplink()) {
            // fetch to see url
            Map<String, IDeeplinkHandler> linkMap = new HashMap<>();
            linkMap.put(Deeplink.TYPE_HOME, new HomeDeeplink(this));
            linkMap.put(Deeplink.TYPE_SCREEN, new ScreenDeeplink(this, mScreens));

            Uri deeplink = getIntent().getData();
            linkMap.get(deeplink.getHost()).handleUri(deeplink);
        }
    }

    private void processMenu(List<ScreenModel> screens) {

        if (mScreens != null) {
            for (ScreenModel screen : mScreens) {
                mNavigationView.getMenu().removeItem(screen.getId().intValue());
            }
        }
        mScreens = screens;
        for (final ScreenModel screen : mScreens) {
            getMenuIcon(screen);
        }
    }

    private void getMenuIcon(final ScreenModel screen) {

        Uri screenIcon = Uri.parse(screen.getIcon());
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(screenIcon)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {

            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {

                if (dataSource.isFinished() && bitmap != null) {
                    Bitmap bmp = Bitmap.createBitmap(bitmap);
                    final Drawable drawable = new BitmapDrawable(getResources(), bmp);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // update screen load status
                            if (!dynamicScreenStatus.isEmpty() && dynamicScreenStatus.containsKey(screen)) {
                                dynamicScreenStatus.put(screen, true);
                                updateScreenLoadStatus();
                            }
                            addMenu(screen, drawable);
                        }
                    });
                }
                dataSource.close();
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                dataSource.getFailureCause().printStackTrace();
                dataSource.close();
            }

            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                super.onNewResultImpl(dataSource);
            }
        }, CallerThreadExecutor.getInstance());
    }

    private void updateScreenLoadStatus() {
        boolean status = true;
        for (ScreenModel screenModel : dynamicScreenStatus.keySet()) {
            if (!dynamicScreenStatus.get(screenModel)) {
                status = false;
            }
        }
        if (status) {
            navigateToDeeplink();
        }
    }

    private void addMenu(final ScreenModel screen, Drawable icon) {
        // just for safety. some times when data change double icon for fiirst time.
        mNavigationView.getMenu().removeItem(screen.getId().intValue());
        final MenuItem menuItem = mNavigationView.getMenu().add(R.id.nav_main_menu,
                screen.getId().intValue(),
                (screen.getOrder()),
                screen.getTitle());
        menuItem.setIcon(icon);
        menuItem.setCheckable(true);
        if (selectedNavMenuId == menuItem.getItemId()) menuItem.setChecked(true);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selectDynamicScreen(menuItem, screen);
                return false;
            }
        });
    }

    private void selectDynamicScreen(MenuItem menuItem, ScreenModel screen) {

        dynamicNavigationMap.put(menuItem.getItemId(), screen.getType().equalsIgnoreCase(DynamicScreen.TYPE_FEED)
                ? FeedScreenFragment.newInstance(screen)
                : BlockScreenFragment.newInstance(screen));
        // perform on click
        dynamicNavigationMap.get(menuItem.getItemId()).initDynamicNavigation(screen, R.id.content_home,
                getSupportFragmentManager());

        updateNavigationAnalytics(menuItem.getItemId(), dynamicNavigationMap.get(menuItem.getItemId()).screenName());
        updateNavigationViewAndReferences(menuItem.getItemId());
    }

    @Override
    public void onUserInfoSent(boolean status) {
        getPreferences().setUserInfoSyncStatus(status);
    }
}
