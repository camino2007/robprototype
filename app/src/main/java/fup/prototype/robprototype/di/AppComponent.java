/*
package fup.prototype.robprototype.di;

import android.app.Application;

import com.rxdroid.api.di.ApiModule;
import com.rxdroid.repository.di.RepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import fup.prototype.data.di.DataModule;
import fup.prototype.robprototype.KtProtoApplication;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        AppModule.class,
        SearchModule.class,
        SearchDetailsModule.class,
        RepositoryModule.class,
        ApiModule.class,
        DataModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(KtProtoApplication protoApplication);

}
*/
