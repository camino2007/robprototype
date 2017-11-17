package fup.prototype.data.models.details;

import android.support.annotation.NonNull;
import fup.prototype.domain.github.model.GitHubRepo;
import io.realm.RealmList;
import io.realm.RealmObject;
import java.util.List;

public class RepositoryEntity extends RealmObject {

    private String idRep;
    private String name;
    private String fullName;

    public RepositoryEntity() {
        //needed for realm
    }

    private RepositoryEntity(Builder builder) {
        idRep = builder.idRep;
        name = builder.name;
        fullName = builder.fullName;
    }

    public String getIdRep() {
        return idRep;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    @NonNull
    public static RealmList<RepositoryEntity> fromDomainModels(final List<GitHubRepo> gitHubRepos) {
        RealmList<RepositoryEntity> realmRepositories = new RealmList<>();
        for (GitHubRepo gitHubRepo : gitHubRepos) {
            RepositoryEntity repository = create(gitHubRepo);
            realmRepositories.add(repository);
        }
        return realmRepositories;
    }

    @NonNull
    private static RepositoryEntity create(GitHubRepo gitHubRepo) {
        return new Builder()
                .name(gitHubRepo.getName())
                .fullName(gitHubRepo.getFullName())
                .idRep(gitHubRepo.getIdRep())
                .build();
    }

    public static final class Builder {
        private String idRep;
        private String name;
        private String fullName;

        public Builder() {
        }

        public Builder idRep(String val) {
            idRep = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder fullName(String val) {
            fullName = val;
            return this;
        }

        public RepositoryEntity build() {
            return new RepositoryEntity(this);
        }
    }


}
