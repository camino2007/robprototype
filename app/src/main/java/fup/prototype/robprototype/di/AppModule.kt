package fup.prototype.robprototype.di

import android.content.Context
import com.rxdroid.api.di.ApiComponent
import com.rxdroid.repository.di.RepositoryComponent
import dagger.Module
import dagger.Provides
import fup.prototype.data.di.DataComponent
import fup.prototype.robprototype.ProtoApplication
import javax.inject.Singleton


/**
 * This is where you will inject application-wide dependencies.
 */
@Module(subcomponents = arrayOf(RepositoryComponent::class, ApiComponent::class, DataComponent::class))
abstract class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: ProtoApplication): Context {
        return application.applicationContext
    }

}