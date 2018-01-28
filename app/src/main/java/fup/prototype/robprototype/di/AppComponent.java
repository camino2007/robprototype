package fup.prototype.robprototype.di;

import android.app.Application;
import com.rxdroid.api.di.ApiModule;
import com.rxdroid.repository.di.RepositoryModule;
import dagger.BindsInstance;
import dagger.Component;
import fup.prototype.data.di.DataModule;
import fup.prototype.robprototype.details.DetailFragment;
import fup.prototype.robprototype.search.MainFragment;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class, ApiModule.class, DataModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(MainFragment mainFragment);

    void inject(DetailFragment detailFragment);

}
