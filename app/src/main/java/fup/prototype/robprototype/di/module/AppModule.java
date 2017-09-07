package fup.prototype.robprototype.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fup.prototype.data.RealmService;
import fup.prototype.data.di.DataComponent;
import fup.prototype.domain.di.DomainComponent;
import fup.prototype.domain.github.provider.GitHubProvider;
import fup.prototype.robprototype.model.UserRepository;
import io.reactivex.annotations.NonNull;

@Module(subcomponents = {DomainComponent.class, DataComponent.class},
        includes = ViewModelModule.class)
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(@NonNull final GitHubProvider gitHubProvider, @NonNull final RealmService realmService) {
        return new UserRepository(gitHubProvider, realmService);
    }

}
