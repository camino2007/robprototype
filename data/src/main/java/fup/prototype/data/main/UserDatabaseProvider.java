package fup.prototype.data.main;

import android.support.annotation.NonNull;

public interface UserDatabaseProvider {

    void storeOrUpdate(@NonNull final UserEntity userEntity);

    UserEntity loadForSearchValue(@NonNull String searchValue);
}
