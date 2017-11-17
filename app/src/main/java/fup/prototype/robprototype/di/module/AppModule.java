package fup.prototype.robprototype.di.module;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.UserRealmProvider;
import fup.prototype.data.di.DataComponent;
import fup.prototype.domain.di.DomainComponent;
import fup.prototype.domain.github.provider.GitHubProvider;
import fup.prototype.domain.github.provider.GitHubUserProvider;
import fup.prototype.robprototype.data.repositories.UserRepository;
import io.reactivex.annotations.NonNull;
import javax.inject.Singleton;

@Module(subcomponents = {DomainComponent.class, DataComponent.class})
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(@NonNull final GitHubProvider gitHubProvider,
                                         @NonNull final GitHubUserProvider gitHubUserProvider,
                                         @NonNull final UserRealmProvider userRealmProvider) {
        return new UserRepository(gitHubProvider, gitHubUserProvider, userRealmProvider);
    }
}
