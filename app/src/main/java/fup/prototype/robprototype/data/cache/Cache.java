package fup.prototype.robprototype.data.cache;


public abstract class Cache<T> {

    private T data;

    public Cache(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean hasCachedData() {
        return data != null;
    }
}
