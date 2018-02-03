package fup.prototype.robprototype.details;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.rxdroid.repository.model.User;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.view.base.NewBaseActivity;
import fup.prototype.robprototype.view.base.fragments.NewBaseFragment;

public class DetailsActivity extends NewBaseActivity {

    private static final String KEY_USER = "keyUser";
    private static final String KEY_BUNDLE = "keyBundle";

    public static Intent createIntent(final Context context, @NonNull final User user) {
        final Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY_USER, user);
        intent.putExtra(KEY_BUNDLE, DetailFragment.createBundle(user));
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected NewBaseFragment createInitialContentFragment() {
        final Intent intent = getIntent();
        return DetailFragment.newInstance(intent.getBundleExtra(KEY_BUNDLE));
    }
}
