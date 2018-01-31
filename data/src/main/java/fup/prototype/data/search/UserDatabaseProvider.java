package fup.prototype.data.search;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Reusable;
import fup.prototype.data.dao.UserDao;
import fup.prototype.data.room.RoomDatabaseProvider;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

@Reusable
public class UserDatabaseProvider extends RoomDatabaseProvider implements UserDao {

    @Inject
    public UserDatabaseProvider(final Context context) {
        super(context);
    }

    @Override
    public Completable insertOrUpdate(@NonNull final UserDto userDto) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                final UserEntity userEntity = new UserEntity();
                userEntity.setName(userDto.name);
                userEntity.setGithubUserId(userDto.githubUserId);
                userEntity.setPublicGistCount(userDto.publicGistCount);
                userEntity.setPublicRepoCount(userDto.publicRepoCount);
                userEntity.setLogin(userDto.login);
                getAppDatabase().userDao().insert(userEntity);
            }
        });
    }

    @Override
    public Maybe<UserDto> getUserForSearchValue(@NonNull String searchValue) {
        return getAppDatabase().userDao()
                .findByLogin(searchValue)
                .map(new Function<UserEntity, UserDto>() {
                    @Override
                    public UserDto apply(final UserEntity userEntity) throws Exception {
                        final UserDto userDto = new UserDto();
                        userDto.login = userEntity.getLogin();
                        userDto.name = userEntity.getName();
                        userDto.githubUserId = userEntity.getGithubUserId();
                        userDto.publicGistCount = userEntity.getPublicGistCount();
                        userDto.publicRepoCount = userEntity.getPublicRepoCount();
                        return userDto;
                    }
                });
    }

    @Override
    public Maybe<List<UserDto>> getAllUserDto() {
        return getAppDatabase().userDao()
                .getAll()
                .map(new Function<List<UserEntity>, List<UserDto>>() {
                    @Override
                    public List<UserDto> apply(List<UserEntity> userEntities) throws Exception {
                        final List<UserDto> userDtos = new ArrayList<>();
                        return userDtos;
                    }
                });
    }

}
