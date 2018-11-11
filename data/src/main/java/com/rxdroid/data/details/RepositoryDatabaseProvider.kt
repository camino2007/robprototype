package com.rxdroid.data.details

import android.content.Context
import com.rxdroid.data.room.RoomDatabaseProvider
import io.reactivex.Completable
import io.reactivex.Maybe


class RepositoryDatabaseProvider(context: Context) : RoomDatabaseProvider(context), UserRepositoryDao {

    override fun insertOrUpdate(userDtoRepositories: List<UserRepositoryDto>): Completable {
        return Completable.fromAction {

            val repositoryEntities = ArrayList<UserRepositoryEntity>()

            for (userDtoRepository in userDtoRepositories) {
                val userRepositoryEntity = UserRepositoryEntity()
                userRepositoryEntity.fullName = userDtoRepository.fullName
                userRepositoryEntity.name = userDtoRepository.name
                userRepositoryEntity.githubUserId = userDtoRepository.githubUserId
                userRepositoryEntity.idRep = userDtoRepository.idRep
                repositoryEntities.add(userRepositoryEntity)
            }
            database.userRepositoryRoomDao().insertAll(repositoryEntities)
        }
    }

    override fun getRepositoriesForUserId(userId: Int): Maybe<List<UserRepositoryDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}