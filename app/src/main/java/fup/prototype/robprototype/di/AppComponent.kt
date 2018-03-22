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
        AppModule::class,
        SearchModule::class,
        SearchDetailModule::class,
        RepositoryModule::class,
        ApiModule::class,
        DataModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: ProtoApplication): Builder

        fun build(): AppComponent
    }

    fun inject(protoApplication: ProtoApplication)

}