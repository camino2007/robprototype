package fup.prototype.data;

import android.support.annotation.NonNull;
import fup.prototype.data.models.main.UserEntity;
import fup.prototype.domain.github.model.GitHubUser;

public class UserRealmProvider implements DatabaseProvider<GitHubUser, UserEntity> {

    private final RealmService realmService;

    public UserRealmProvider(@NonNull final RealmService realmService) {
        this.realmService = realmService;
    }

    @Override
    public void onStoreOrUpdate(@NonNull final GitHubUser gitHubUser) {
        final UserEntity userEntity = UserEntity.fromDomainModel(gitHubUser);
        realmService.getRealm().beginTransaction();
        realmService.getRealm().copyToRealmOrUpdate(userEntity);
        realmService.getRealm().commitTransaction();
    }

    @Override
    public UserEntity getForStringValue(@NonNull final String value) {
        return realmService.getRealm().where(UserEntity.class).equalTo(RealmTable.User.LOGIN, value).findFirst();
    }
}
