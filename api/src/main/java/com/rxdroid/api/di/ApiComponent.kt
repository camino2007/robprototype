package com.rxdroid.api.di

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ApiModule::class))
interface ApiComponent {

    @Subcomponent.Builder
    interface Builder {

        fun build(): ApiComponent
    }

}