package fup.prototype.data.main;

import android.support.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface UserDatabaseProvider {

    Completable insertOrUpdate(@NonNull final UserDto userDto);

    Maybe<UserDto> getForSearchValue(@NonNull String searchValue);
}
