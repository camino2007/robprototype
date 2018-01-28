package fup.prototype.data;

import android.support.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import java.util.List;

public interface DatabaseProvider<T> {

    Completable insertOrUpdate(@NonNull final T t);

    Maybe<T> getForSearchValue(@NonNull String searchValue);

    Maybe<List<T>> getAll();
}
