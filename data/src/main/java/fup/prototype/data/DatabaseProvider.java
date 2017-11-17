package fup.prototype.data;

import android.support.annotation.NonNull;

/**
 * @param <AM> API model
 * @param <DE> data base entity
 */
public interface DatabaseProvider<AM, DE> {

    void onStoreOrUpdate(@NonNull final AM AM);

    DE getForStringValue(@NonNull final String value);
}
