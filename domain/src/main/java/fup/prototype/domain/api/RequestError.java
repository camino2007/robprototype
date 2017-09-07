package fup.prototype.domain.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import retrofit2.Response;

public class RequestError {

    @Nullable
    private final Response<?> response;

    @Nullable
    private final Throwable throwable;

    private RequestError(@Nullable Response<?> response, @Nullable Throwable throwable) {
        this.response = response;
        this.throwable = throwable;
    }

    @NonNull
    public static RequestError create(@Nullable final Response<?> response, @Nullable final Throwable throwable) {
        return new RequestError(response, throwable);
    }

    @Nullable
    public Response<?> getResponse() {
        return response;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }
}
