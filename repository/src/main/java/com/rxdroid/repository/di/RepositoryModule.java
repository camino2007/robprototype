package com.rxdroid.repository.di;

import com.rxdroid.api.di.ApiComponent;
import com.rxdroid.repository.GithubDetailsUiRepository;
import com.rxdroid.repository.UiRepository;
import com.rxdroid.repository.UserUiRepository;
import dagger.Module;
import dagger.Provides;
import fup.prototype.data.di.DataComponent;

@Module(subcomponents = {ApiComponent.class, DataComponent.class})
public class RepositoryModule {

    @Provides
    UiRepository provideUserUiRepository(final UserUiRepository userUiRepository) {
        return userUiRepository;
    }

    @Provides
    UiRepository provideGithubDetailsUiRepository(final GithubDetailsUiRepository detailsUiRepository) {
        return detailsUiRepository;
    }
}


