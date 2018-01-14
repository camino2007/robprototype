package fup.prototype.data.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmService {

    private Realm realm;

    public RealmService(@NonNull final Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //TODO add Realm migration
    /*
     https://github.com/realm/realm-java/blob/master/examples/migrationExample/src/main/java/io/realm/examples/realmmigrationexample/model/Migration.java
     try {
            Realm.migrateRealm(config0, new Migration());
        } catch (FileNotFoundException ignored) {
            // If the Realm file doesn't exist, just ignore.
        }*/


        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfiguration);
    }

    public Realm getRealm() {
        return realm;
    }
}
