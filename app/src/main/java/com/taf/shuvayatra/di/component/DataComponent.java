package com.taf.shuvayatra.di.component;

import com.taf.data.di.PerActivity;
import com.taf.shuvayatra.di.module.ActivityModule;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.ui.activity.SplashScreenActivity;
import com.taf.shuvayatra.ui.fragment.HomeFeedFragment;

import dagger.Component;

/**
 * Created by julian on 12/7/15.
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, DataModule.class}
)
public interface DataComponent extends ActivityComponent {
    void inject(SplashScreenActivity pActivity);
}
