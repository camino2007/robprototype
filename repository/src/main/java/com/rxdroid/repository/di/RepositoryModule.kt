package com.rxdroid.repository.di

import com.rxdroid.api.di.ApiComponent
import com.rxdroid.repository.repositories.UiRepository
import com.rxdroid.repository.repositories.detail.DetailsUiRepository
import com.rxdroid.repository.repositories.search.UserUiRepository
import dagger.Module
import dagger.Provides
import fup.prototype.data.di.DataComponent

@Module(subcomponents = arrayOf(ApiComponent::class, DataComponent::class))
class RepositoryModule {

    @Provides
    fun provideUserUiRepository(userUiRepository: UserUiRepository): UiRepository<*> {
        return userUiRepository
    }

    @Provides
    fun provideGithubDetailsUiRepository(detailsUiRepository: DetailsUiRepository): UiRepository<*> {
        return detailsUiRepository
    }

}