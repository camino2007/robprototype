package fup.prototype.robprototype.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fup.prototype.robprototype.R;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private static final String KEY_FRAGMENT = "keyFragment";

    private BaseFragment baseFragment;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate - savedInstanceState != null ");
            final String keyFragment = savedInstanceState.getString(KEY_FRAGMENT);
            baseFragment = (BaseFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, keyFragment);
        } else {
            Log.d(TAG, "onCreate - savedInstanceState == null ");
        }
        initContent();

    }

    private void initContent() {
        if (baseFragment == null) {
            baseFragment = createInitialContentFragment();
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, baseFragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FRAGMENT, baseFragment.getKey());
        getSupportFragmentManager().putFragment(outState, baseFragment.getKey(), baseFragment);
    }

    protected abstract int getLayoutId();

    protected abstract BaseFragment createInitialContentFragment();

}
