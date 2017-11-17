package fup.prototype.data.di;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.RealmService;
import fup.prototype.data.UserRealmProvider;
import javax.inject.Singleton;

@Module
public class DataModule {

    @Provides
    @Singleton
    RealmService provideRealmService(Context context) {
        return new RealmService(context);
    }

    @Provides
    UserRealmProvider provideUserRealmProvider(@NonNull final RealmService realmService) {
        return new UserRealmProvider(realmService);
    }
}


