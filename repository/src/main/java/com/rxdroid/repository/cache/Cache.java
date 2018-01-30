package com.rxdroid.repository.cache;

public interface Cache<T> {

    void setData(T data);

    T getData();

    boolean hasValidCachedData();

    boolean isSameObjectCached(T t);

}
