package fup.prototype.robprototype.view.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.view.base.fragments.NewBaseFragment;

public abstract class NewBaseActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private static final String KEY_FRAGMENT = "keyFragment";

    @Inject
    protected DispatchingAndroidInjector<Fragment> fragmentInjector;

    private NewBaseFragment baseFragment;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        baseFragment = getOrCreateFragment(savedInstanceState);
        initContent();
    }

    private NewBaseFragment getOrCreateFragment(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            final String keyFragment = savedInstanceState.getString(KEY_FRAGMENT);
            return (NewBaseFragment) getSupportFragmentManager().getFragment(savedInstanceState, keyFragment);
        } else {
            return createInitialContentFragment();
        }
    }

    private void initContent() {
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

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    protected abstract int getLayoutId();

    protected abstract NewBaseFragment createInitialContentFragment();
}
