package com.rxdroid.data.details;


import android.content.Context;
import android.support.annotation.NonNull;

import com.rxdroid.data.dao.RepositoryDao;
import com.rxdroid.data.room.RoomDatabaseProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Action;

public class RepositoryDatabaseProvider extends RoomDatabaseProvider implements RepositoryDao {

    public RepositoryDatabaseProvider(Context context) {
        super(context);
    }


    @Override
    public Completable insertOrUpdate(@NonNull final List<RepositoryDto> repositoryDtos) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                final List<RepositoryEntity> repositoryEntities = new ArrayList<>();
                RepositoryEntity entity;
                for (RepositoryDto dto : repositoryDtos) {
                    entity = new RepositoryEntity();
                    entity.setFullName(dto.fullName);
                    entity.setGithubUserId(dto.githubUserId);
                    entity.setIdRep(dto.idRep);
                    entity.setName(dto.name);
                    repositoryEntities.add(entity);
                }
                getAppDatabase().repositoryRoomDao().insertAll(repositoryEntities);
            }
        });
    }

    @Override
    public Maybe<List<RepositoryDto>> getRepositoriesForUserLogin(final int userId) {
        //TODO
        return null;
    }
}
