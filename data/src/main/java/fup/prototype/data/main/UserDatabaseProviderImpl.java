package fup.prototype.data.main;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Reusable;
import fup.prototype.data.DatabaseProvider;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import javax.inject.Inject;

@Reusable
public class UserDatabaseProviderImpl extends DatabaseProvider<UserDto> implements UserDatabaseProvider {

    @Inject
    public UserDatabaseProviderImpl(final Context context) {
        super(context);
    }

    @Override
    public Completable insertOrUpdate(@NonNull final UserDto userDto) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                final UserEntity userEntity = new UserEntity.Builder().login(userDto.login)
                                                                      .publicGistCount(userDto.publicGistCount)
                                                                      .name(userDto.name)
                                                                      .publicRepoCount(userDto.publicRepoCount)
                                                                      .build();
                getAppDatabase().userDao().insert(userEntity);
            }
        });
    }

    @Override
    public Maybe<UserDto> getForSearchValue(@NonNull final String searchValue) {
        return getAppDatabase().userDao().findByLogin(searchValue).map(new Function<UserEntity, UserDto>() {
            @Override
            public UserDto apply(final UserEntity userEntity) throws Exception {
                final UserDto userDto = new UserDto();
                userDto.login = userEntity.getLogin();
                userDto.name = userEntity.getName();
                userDto.publicGistCount = userEntity.getPublicGistCount();
                userDto.publicRepoCount = userEntity.getPublicRepoCount();
                return userDto;
            }
        });
    }
}
