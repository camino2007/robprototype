package fup.prototype.robprototype.di

import com.rxdroid.api.di.ApiModule
import com.rxdroid.repository.di.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import fup.prototype.data.di.DataModule
import fup.prototype.robprototype.ProtoApplication
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class,
        KtAppModule::class,
        SearchModule::class,
        //SearchDetailsModule::class,
        RepositoryModule::class,
        ApiModule::class,
        DataModule::class))
interface KtAppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: ProtoApplication): Builder

        fun build(): KtAppComponent
    }

    fun inject(protoApplication: ProtoApplication)

/*    @Singleton
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

        void inject(KtProtoApplication protoApplication);*/
}