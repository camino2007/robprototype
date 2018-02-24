package com.rxdroid.repository.di

import com.rxdroid.api.di.ApiModule
import dagger.Subcomponent
import fup.prototype.data.di.DataModule

@Subcomponent(modules = arrayOf(RepositoryModule::class, ApiModule::class, DataModule::class))
interface RepositoryComponent {

    @Subcomponent.Builder
    interface Builder {

        fun build(): RepositoryComponent
    }
}