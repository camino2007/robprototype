package com.rxdroid.data.search

import android.content.Context
import com.rxdroid.data.room.RoomDatabaseProvider
import io.reactivex.Completable
import io.reactivex.Maybe

class UserDatabaseProvider(context: Context) : RoomDatabaseProvider(context), UserDao {

    override fun insertOrUpdate(userDto: UserDto): Completable {
        return Completable.fromAction {
            val userEntity = UserEntity()
            userEntity.name = userDto.name
            userEntity.login = userDto.login
            userEntity.githubUserId = userDto.githubUserId
            userEntity.publicGistCount = userDto.publicGistCount
            userEntity.publicRepoCount = userDto.publicRepoCount
            database.userDaoRoom().insert(userEntity)
        }
    }

    override fun getUserForSearchValue(searchValue: String): Maybe<UserDto> {
        return database.userDaoRoom().findByLogin(searchValue).map { userEntity: UserEntity ->
            UserDto(userEntity.login, userEntity.name, userEntity.githubUserId, userEntity.publicRepoCount, userEntity.publicGistCount)
        } as Maybe<UserDto>
    }

}
