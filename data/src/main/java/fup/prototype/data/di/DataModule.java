package fup.prototype.data.di;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.main.UserDatabaseProvider;
import fup.prototype.data.main.UserDatabaseProviderImpl;
import fup.prototype.data.realm.RealmService;
import javax.inject.Singleton;

@Module
public class DataModule {

    @Provides
    @Singleton
    RealmService provideRealmService(Context context) {
        return new RealmService(context);
    }

    @Provides
    UserDatabaseProvider provideUserDatabaseProvider(@NonNull final UserDatabaseProviderImpl userDatabaseProvider) {
        return userDatabaseProvider;
    }
}


