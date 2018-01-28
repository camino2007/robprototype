package fup.prototype.robprototype.di;

import android.app.Application;
import android.content.Context;
import com.rxdroid.api.di.ApiComponent;
import com.rxdroid.repository.di.RepositoryComponent;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.di.DataComponent;
import fup.prototype.robprototype.details.DetailViewModel;
import fup.prototype.robprototype.search.MainViewModel;
import javax.inject.Singleton;

@Module(subcomponents = {RepositoryComponent.class, ApiComponent.class, DataComponent.class})
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    MainViewModel provideMainViewModel(final MainViewModel mainViewModel) {
        return mainViewModel;
    }

    @Provides
    DetailViewModel provideDetailViewModel(final DetailViewModel detailViewModel) {
        return detailViewModel;
    }
}
