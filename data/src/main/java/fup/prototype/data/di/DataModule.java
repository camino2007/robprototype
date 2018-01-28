package fup.prototype.data.di;

import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.DatabaseProvider;
import fup.prototype.data.search.UserDatabaseProvider;

@Module
public class DataModule {

    @Provides
    DatabaseProvider provideUserDatabaseProvider(@NonNull final UserDatabaseProvider userDatabaseProvider) {
        return userDatabaseProvider;
    }
}


