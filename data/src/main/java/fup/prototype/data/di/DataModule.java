package fup.prototype.data.di;

import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.main.UserDatabaseProvider;
import fup.prototype.data.main.UserDatabaseProviderImpl;

@Module
public class DataModule {

    @Provides
    UserDatabaseProvider provideUserDatabaseProvider(@NonNull final UserDatabaseProviderImpl userDatabaseProvider) {
        return userDatabaseProvider;
    }
}


