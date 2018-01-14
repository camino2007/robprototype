package com.rxdroid.api.di;

import dagger.Subcomponent;

@Subcomponent(modules = ApiModule.class)
public interface ApiComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder requestModule(ApiModule module);

        ApiComponent build();
    }
}
