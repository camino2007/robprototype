package com.rxdroid.repository.di;

import com.rxdroid.api.di.ApiModule;
import dagger.Subcomponent;
import fup.prototype.data.di.DataModule;

@Subcomponent(modules = {RepositoryModule.class, ApiModule.class, DataModule.class})
public interface RepositoryComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder requestModule(RepositoryModule module);

        RepositoryComponent build();
    }
}
