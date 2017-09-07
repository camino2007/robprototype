package fup.prototype.robprototype;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import fup.prototype.robprototype.di.AppInjector;

public class ProtoApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return androidInjector;
    }

}
