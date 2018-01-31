package fup.prototype.data.dao;


import android.support.annotation.NonNull;

import java.util.List;

import fup.prototype.data.DatabaseDao;
import fup.prototype.data.details.RepositoryDto;
import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface RepositoryDao extends DatabaseDao {

    Completable insertOrUpdate(@NonNull final List<RepositoryDto> repositoryDtos);

    Maybe<List<RepositoryDto>> getRepositoriesForUserLogin(final int userId);

}
