package fup.prototype.robprototype.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import fup.prototype.data.di.DataModule;
import fup.prototype.domain.di.DomainModule;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.di.module.AppModule;
import fup.prototype.robprototype.view.viewmodels.MainViewModel;

@Singleton
@Component(modules = {
        AppModule.class,
        DataModule.class,
        DomainModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(ProtoApplication protoApplication);

    void inject(MainViewModel mainViewModel);

}
