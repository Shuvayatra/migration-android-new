package com.taf.shuvayatra.di.component;

import com.taf.data.di.PerActivity;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.receivers.DownloadStateReceiver;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.PlacesDetailActivity;
import com.taf.shuvayatra.ui.activity.SplashScreenActivity;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.shuvayatra.ui.fragment.JourneyFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, DataModule.class}
)
public interface DataComponent extends ActivityComponent {
    void inject(SplashScreenActivity pActivity);
    void inject(PlacesDetailActivity pActivity);
    void inject(AudioDetailActivity pActivity);
    void inject(FeedFragment pFragment);
    void inject(JourneyFragment pFragment);
    void inject(DestinationFragment pFragment);
    void inject(DownloadStateReceiver pReceiver);
}
