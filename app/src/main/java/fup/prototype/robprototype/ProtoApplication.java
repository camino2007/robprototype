package fup.prototype.robprototype;

import android.app.Application;

import fup.prototype.robprototype.di.component.AppComponent;
import fup.prototype.robprototype.di.component.DaggerAppComponent;

public class ProtoApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().application(this).build();
        }
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
