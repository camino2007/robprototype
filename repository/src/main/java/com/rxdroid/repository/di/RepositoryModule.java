package com.rxdroid.repository.di;

import com.rxdroid.api.di.ApiComponent;
import com.rxdroid.repository.Repository;
import com.rxdroid.repository.UserRepository;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.di.DataComponent;

@Module(subcomponents = {ApiComponent.class, DataComponent.class})
public class RepositoryModule {

    @Provides
    Repository provideUserRepository(final UserRepository userRepository) {
        return userRepository;
    }

/*    @Provides
    @Singleton
    RealmService provideRealmService(Context context) {
        return new RealmService(context);
    }

    @Provides
    UserRealmProvider provideUserRealmProvider(@NonNull final RealmService realmService) {
        return new UserRealmProvider(realmService);
    }*/
}


