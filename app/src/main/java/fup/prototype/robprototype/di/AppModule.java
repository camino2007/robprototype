package fup.prototype.robprototype.di;

import android.app.Application;
import android.content.Context;
import com.rxdroid.api.di.ApiComponent;
import com.rxdroid.repository.di.RepositoryComponent;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.di.DataComponent;
import javax.inject.Singleton;

@Module(subcomponents = {RepositoryComponent.class, ApiComponent.class, DataComponent.class})
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }
}
