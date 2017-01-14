package com.taf.shuvayatra.di.component;

import com.taf.data.di.PerActivity;
import com.taf.shuvayatra.base.CategoryDetailActivity;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.gcm.MyFcmListenerService;
import com.taf.shuvayatra.receivers.DownloadStateReceiver;
import com.taf.shuvayatra.service.MyTaskService;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.DestinationDetailActivity;
import com.taf.shuvayatra.ui.activity.FeedActivity;
import com.taf.shuvayatra.ui.activity.HomeActivity;
import com.taf.shuvayatra.ui.activity.OnBoardActivity;
import com.taf.shuvayatra.ui.activity.PodcastsActivity;
import com.taf.shuvayatra.base.PostDetailActivity;
import com.taf.shuvayatra.ui.activity.SearchActivity;
import com.taf.shuvayatra.ui.deprecated.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.deprecated.activity.FacebookParseActivity;
import com.taf.shuvayatra.ui.deprecated.activity.PlacesDetailActivity;
import com.taf.shuvayatra.ui.deprecated.activity.SearchListActivity;
import com.taf.shuvayatra.ui.deprecated.activity.SplashScreenActivity;
import com.taf.shuvayatra.ui.deprecated.activity.TagListActivity;
import com.taf.shuvayatra.ui.deprecated.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.deprecated.fragment.FeedFragment;
import com.taf.shuvayatra.ui.deprecated.fragment.InfoFragment;
import com.taf.shuvayatra.ui.deprecated.fragment.NotificationsFragment;
import com.taf.shuvayatra.ui.fragment.BlockScreenFragment;
import com.taf.shuvayatra.ui.fragment.ChannelFragment;
import com.taf.shuvayatra.ui.fragment.CountryWidgetFragment;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.FavouritePostFragment;
import com.taf.shuvayatra.ui.fragment.FeedScreenFragment;
import com.taf.shuvayatra.ui.fragment.HomeFragment;
import com.taf.shuvayatra.ui.fragment.JourneyFragment;
import com.taf.shuvayatra.ui.fragment.NewsFragment;
import com.taf.shuvayatra.ui.fragment.UserAccountFragment;
import com.taf.shuvayatra.ui.fragment.onboarding.CountryFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, DataModule.class}
)
public interface DataComponent extends ActivityComponent {
    void inject(SplashScreenActivity pActivity);

    void inject(PlacesDetailActivity pActivity);

    void inject(OnBoardActivity pActivity);

    void inject(CountryFragment pFragment);

    void inject(com.taf.shuvayatra.ui.deprecated.activity.AudioDetailActivity activity);

    void inject(ArticleDetailActivity pActivity);

    void inject(VideoDetailActivity pActivity);

    void inject(TagListActivity pActivity);

    void inject(FeedFragment pFragment);

    void inject(com.taf.shuvayatra.ui.deprecated.fragment.JourneyFragment pFragment);

    void inject(com.taf.shuvayatra.ui.deprecated.fragment.DestinationFragment pFragment);

    void inject(DownloadStateReceiver pReceiver);

    void inject(CategoryDetailActivity pActivity);

    void inject(MyFcmListenerService pService);

    void inject(NotificationsFragment pFragment);

    void inject(InfoFragment pFragment);

    void inject(FacebookParseActivity pActivity);

    void inject(SearchListActivity pActivity);

    void inject(HomeFragment fragment);

    void inject(CountryWidgetFragment fragment);

    void inject(JourneyFragment fragment);

    void inject(FeedActivity activity);

    void inject(PodcastsActivity activity);

    void inject(DestinationFragment fragment);

    void inject(PostDetailActivity activity);

    void inject(AudioDetailActivity activity);

    void inject(DestinationDetailActivity activity);

    void inject(MyTaskService service);

    void inject(ChannelFragment fragment);

    void inject(SearchActivity activity);

    void inject(NewsFragment fragment);

    void inject(UserAccountFragment userAccountFragment);

    void inject(FavouritePostFragment favouritePostFragment);

    void inject(BlockScreenFragment blockScreenFragment);

    void inject(FeedScreenFragment feedScreenFragment);

    void inject(HomeActivity homeActivity);
}
