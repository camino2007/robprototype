package fup.prototype.data.main;

import android.support.annotation.NonNull;
import dagger.Reusable;
import fup.prototype.data.DatabaseProvider;
import fup.prototype.data.realm.RealmService;
import fup.prototype.data.realm.RealmTable;
import javax.inject.Inject;

@Reusable
public class UserDatabaseProviderImpl extends DatabaseProvider<UserEntity> implements UserDatabaseProvider {

    @Inject
    public UserDatabaseProviderImpl(final RealmService realmService) {
        super(realmService);
    }

    @Override
    public void storeOrUpdate(@NonNull final UserEntity userEntity) {
        getRealmService().getRealm().beginTransaction();
        getRealmService().getRealm().copyToRealmOrUpdate(userEntity);
        getRealmService().getRealm().commitTransaction();
    }

    @Override
    public UserEntity loadForSearchValue(@NonNull final String value) {
/*        return Observable.just(value).map(new Function<String, UserEntity>() {
            @Override
            public UserEntity apply(final String searchValue) throws Exception {
                return getRealmService().getRealm().where(UserEntity.class).equalTo(RealmTable.User.LOGIN, searchValue).findFirst();
            }
        });*/

        return getRealmService().getRealm().where(UserEntity.class).equalTo(RealmTable.User.LOGIN, value).findFirst();
    }
}
