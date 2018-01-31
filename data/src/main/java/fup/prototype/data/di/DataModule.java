package fup.prototype.data.di;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import fup.prototype.data.DatabaseDao;
import fup.prototype.data.details.RepositoryDatabaseProvider;
import fup.prototype.data.search.UserDatabaseProvider;

@Module
public class DataModule {

    @Provides
    DatabaseDao provideUserDatabaseProvider(@NonNull final UserDatabaseProvider userDatabaseProvider) {
        return userDatabaseProvider;
    }

    @Provides
    DatabaseDao provideRepositoryDatabaseProvider(@NonNull final RepositoryDatabaseProvider repositoryDatabaseProvider) {
        return repositoryDatabaseProvider;
    }
}


