package com.taf.shuvayatra.di.component;

import android.app.Activity;

import com.taf.data.di.PerActivity;
import com.taf.shuvayatra.di.module.ActivityModule;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent {
    Activity getActivity();
}
