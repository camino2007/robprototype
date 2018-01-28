package fup.prototype.data.details;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import fup.prototype.data.search.UserEntity;

@Entity(foreignKeys = @ForeignKey(entity = UserEntity.class, parentColumns = "id", childColumns = "user_id"))
public class RepositoryEntityNew {

    @NonNull
    @PrimaryKey
    private String idRep;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "full_name")
    private String fullName;

    public String getIdRep() {
        return idRep;
    }

    public void setIdRep(final String idRep) {
        this.idRep = idRep;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }
}
