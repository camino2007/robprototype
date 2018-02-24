package com.rxdroid.repository.di

import com.rxdroid.api.di.ApiComponent
import com.rxdroid.repository.GithubDetailsUiRepository
import com.rxdroid.repository.UiRepository
import com.rxdroid.repository.UserUiRepository
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
    fun provideGithubDetailsUiRepository(detailsUiRepository: GithubDetailsUiRepository): UiRepository<*> {
        return detailsUiRepository
    }

}