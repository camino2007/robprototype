package fup.prototype.robprototype.di.component;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import fup.prototype.data.di.DataModule;
import fup.prototype.domain.di.DomainModule;
import fup.prototype.robprototype.di.module.AppModule;
import fup.prototype.robprototype.view.main.viewmodels.MainViewModel;
import javax.inject.Singleton;

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

    void inject(MainViewModel mainViewModel);

}
