package com.rxdroid.data.dao;


import android.support.annotation.NonNull;

import java.util.List;

import com.rxdroid.data.DatabaseDao;
import com.rxdroid.data.search.UserDto;
import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface UserDao extends DatabaseDao {

    Completable insertOrUpdate(@NonNull final UserDto userDto);

    Maybe<UserDto> getUserForSearchValue(@NonNull final String searchValue);

    Maybe<List<UserDto>> getAllUserDto();
}
