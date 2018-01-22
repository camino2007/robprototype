package com.rxdroid.repository.cache;


public abstract class Cache<T> {

    private T data;

    public boolean hasCachedData() {
        return data != null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
