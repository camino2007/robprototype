package fup.prototype.robprototype.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.di.AppComponent;
import fup.prototype.robprototype.di.HasComponent;
import fup.prototype.robprototype.view.base.fragments.BaseFragment;

public abstract class BaseActivity extends AppCompatActivity implements HasComponent<AppComponent> {

    private static final String KEY_FRAGMENT = "keyFragment";

    private BaseFragment baseFragment;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        baseFragment = getOrCreateFragment(savedInstanceState);
        initContent();
    }

    private BaseFragment getOrCreateFragment(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            final String keyFragment = savedInstanceState.getString(KEY_FRAGMENT);
            return (BaseFragment) getSupportFragmentManager().getFragment(savedInstanceState, keyFragment);
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
    public AppComponent getAppComponent() {
        return ProtoApplication.getAppComponent();
    }

    protected abstract int getLayoutId();

    protected abstract BaseFragment createInitialContentFragment();
}
