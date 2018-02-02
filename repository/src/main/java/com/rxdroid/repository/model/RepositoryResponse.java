package com.rxdroid.repository.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.rxdroid.api.error.RequestError;

import java.util.List;

@AutoValue
public abstract class RepositoryResponse {

    @Nullable
    public abstract List<Repository> getRepositories();

    @Nullable
    public abstract RequestError getRequestError();

    @NonNull
    public static RepositoryResponse create(final List<Repository> repositories, final RequestError requestError) {
        return new AutoValue_RepositoryResponse(repositories, requestError);
    }
}
