package com.rxdroid.repository;

import android.support.annotation.NonNull;

public interface Repository {

    void loadFromApi(@NonNull final String userName);

    void loadFromDatabase(@NonNull final String userName);

    boolean hasCachedValue();
}
