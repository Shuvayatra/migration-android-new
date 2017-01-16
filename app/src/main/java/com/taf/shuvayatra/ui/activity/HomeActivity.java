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
import android.support.v4.app.Fragment;
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
import com.taf.model.ScreenModel;
import com.taf.model.UserInfoModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.MediaServiceActivity;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.HomeActivityPresenter;
import com.taf.shuvayatra.presenter.OnBoardingPresenter;
import com.taf.shuvayatra.ui.fragment.BlockScreenFragment;
import com.taf.shuvayatra.ui.fragment.ChannelFragment;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.FeedScreenFragment;
import com.taf.shuvayatra.ui.fragment.HomeFragment;
import com.taf.shuvayatra.ui.fragment.UserAccountFragment;
import com.taf.shuvayatra.ui.views.HomeActivityView;
import com.taf.shuvayatra.ui.views.OnBoardingView;
import com.taf.util.MyConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

import static com.taf.util.MyConstants.Extras.KEY_ABOUT;
import static com.taf.util.MyConstants.Extras.KEY_CONTACT_US;
import static com.taf.util.MyConstants.Extras.KEY_INFO;

public class HomeActivity extends MediaServiceActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeActivityView,
        OnBoardingView {

    public static final String TAG = "HomeActivity";
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

    List<ScreenModel> mScreens;
    int selectedNavMenuId;

    private BroadcastReceiver mRadioCallbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showFragment(R.id.nav_radio);
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
            List<ScreenModel> screens = (List<ScreenModel>) savedInstanceState.get(STATE_SCREENS);
            processMenu(screens);
        } else {
            selectedNavMenuId = R.id.nav_home;
            homeActivityPresenter.initialize(null);
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
        if (!countryInfo.equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION) && !countryInfo.equalsIgnoreCase(getString(R.string.country_not_decided_yet))) {

            userInfo.setDestinedCountry(TextUtils.split(countryInfo, ",")[2]);
        } else {
            userInfo.setDestinedCountry(null);
        }
        Locale locale = Locale.getDefault();
        userInfo.setWorkStatus(getPreferences().getPreviousWorkStatus());
        int id = getPreferences().getOriginalLocation();

        String[] zones = getResources().getStringArray(R.array.zones);
        userInfo.setOrignalLocation(zones[id]);
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
        IntentFilter filter = new IntentFilter(MyConstants.Intent.ACTION_SHOW_RADIO);
        registerReceiver(mRadioCallbackReceiver, filter);
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
                    .replace(R.id.content_home, HomeFragment.getInstance(), HomeFragment.TAG)
                    .commit();
            return;
        }
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        showFragment(id);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    Replace the fragment by their menu Id from navigation drawer
     */

    private void showFragment(int menuId) {
        Fragment fragment = null;
        mNavigationView.setCheckedItem(menuId);
        Intent intent = new Intent(this, InfoActivity.class);

        switch (menuId) {
            case R.id.nav_home:
                fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
                if (fragment == null) {
                    fragment = HomeFragment.getInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, HomeFragment.TAG)
                        .commit();
                break;
//            case R.id.nav_journey:
//                fragment = getSupportFragmentManager().findFragmentByTag(JourneyFragment.TAG);
//                if (fragment == null) {
//                    fragment = JourneyFragment.getInstance();
//                }
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_home, fragment, JourneyFragment.TAG)
//                        .commit();
//                break;
            case R.id.nav_radio:
                fragment = getSupportFragmentManager().findFragmentByTag(ChannelFragment.TAG);
                if (fragment == null) {
                    fragment = ChannelFragment.getInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, ChannelFragment.TAG)
                        .commit();
                break;
            case R.id.nav_destination:
                fragment = getSupportFragmentManager().findFragmentByTag(DestinationFragment.TAG);
                if (fragment == null) {
                    fragment = DestinationFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, DestinationFragment.TAG)
                        .commit();
                break;
//            case R.id.nav_news:
//                fragment = getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG);
//                if (fragment == null) {
//                    fragment = NewsFragment.newInstance();
//                }
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_home, fragment, NewsFragment.TAG)
//                        .commit();
//                break;
            case R.id.nav_account:
                fragment = getSupportFragmentManager().findFragmentByTag(UserAccountFragment.TAG);
                if (fragment == null) {
                    fragment = UserAccountFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home, fragment, UserAccountFragment.TAG)
                        .commit();
                break;
            case R.id.nav_about:
                intent.putExtra(KEY_INFO, KEY_ABOUT);
                startActivity(intent);
                break;
            case R.id.nav_contact:
                intent.putExtra(KEY_INFO, KEY_CONTACT_US);
                startActivity(intent);
                break;

        }
        if (menuId == R.id.nav_account) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(0);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(getResources().getDimensionPixelOffset(R.dimen.spacing_xsmall));
            }
        }
        Logger.e(TAG,"fragment: "+ fragment);
        if (fragment != null)
            selectedNavMenuId = menuId;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_MENU_ID, selectedNavMenuId);
        outState.putSerializable(STATE_SCREENS, (Serializable) mScreens);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showErrorView(String pErrorMessage) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void renderScreens(List<ScreenModel> screens) {
        processMenu(screens);
    }

    private void processMenu(List<ScreenModel> screens) {

        Logger.e(TAG, "mScreens.size(): " + mScreens.size());
        // TODO: 1/16/17 refactor code 
//        mNavigationView.getMenu().removeGroup(R.id.nav_main_menu);
        if(mScreens != null){
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

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(screen.getIcon()))
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {

            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                if (dataSource.isFinished() && bitmap != null) {
                    Logger.e(TAG, "bitmap has come");
//                    Bitmap bmp = Bitmap.createBitmap(bitmap);
                    final Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            addMenu(screen, drawable);
                        }
                    });
                    dataSource.close();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Logger.e(TAG, "Bitmap error: ");
                dataSource.getFailureCause().printStackTrace();
                if (dataSource != null) {
                    dataSource.close();
                }
            }

            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                super.onNewResultImpl(dataSource);
            }
        }, CallerThreadExecutor.getInstance());
    }

    private void addMenu(final ScreenModel screen, Drawable icon) {
        Logger.e(TAG, "menu added: " + screen.getId());
        // just for safety. some times when data change double icon for fiirst time.
        mNavigationView.getMenu().removeItem(screen.getId().intValue());
        final MenuItem menuItem = mNavigationView.getMenu().add(R.id.nav_main_menu,
                screen.getId().intValue(),
                (screen.getOrder()),
                screen.getTitle());
        menuItem.setIcon(icon);
        menuItem.setCheckable(true);
        if(selectedNavMenuId == menuItem.getItemId()) menuItem.setChecked(true);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment = null;
                Logger.e(TAG, "screen.getType(): " + screen.getType());
                if (screen.getType().equals("block")) {
                    fragment = getSupportFragmentManager().findFragmentByTag(BlockScreenFragment.TAG + screen.getId());
                    if (fragment == null) {
                        fragment = BlockScreenFragment.newInstance(screen);
                    }

                } else if (screen.getType().equals("feed")) {
                    fragment = getSupportFragmentManager().findFragmentByTag(FeedScreenFragment.TAG + screen.getId());
                    if (fragment == null) {
                        fragment = FeedScreenFragment.newInstance(screen);
                    }
                }
                if (fragment != null) {
                    selectedNavMenuId = menuItem.getItemId();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_home, fragment, FeedScreenFragment.TAG + screen.getId())
                            .commit();
                    mDrawer.closeDrawer(GravityCompat.START);
                    menuItem.setChecked(true);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSendUserInfo() {
        // TODO: 1/15/17 alter preference
    }
}
