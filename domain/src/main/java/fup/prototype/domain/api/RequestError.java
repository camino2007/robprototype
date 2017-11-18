package fup.prototype.domain.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import retrofit2.Response;

public class RequestError {

    public static final int ERROR_CODE_NO_SEARCH_INPUT = 102;

    @Nullable
    private final Response<?> response;

    @Nullable
    private final Throwable throwable;

    /**
     * Can be used to create a custom, non-Api related error.
     */
    private int errorCode;

    private RequestError(@Nullable Response<?> response, @Nullable Throwable throwable, final int errorCode) {
        this.response = response;
        this.throwable = throwable;
        this.errorCode = errorCode;
    }

    @NonNull
    public static RequestError create(@Nullable final Response<?> response, @Nullable final Throwable throwable) {
        return new RequestError(response, throwable, -1);
    }

    @NonNull
    public static RequestError create(final int errorCode) {
        return new RequestError(null, null, errorCode);
    }

    @Nullable
    public Response<?> getResponse() {
        return response;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
