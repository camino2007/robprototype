package fup.prototype.data.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fup.prototype.data.RealmService;

@Module
public class DataModule {

    @Provides
    @Singleton
    RealmService provideRealmService(Context context) {
        return new RealmService(context);
    }
}


