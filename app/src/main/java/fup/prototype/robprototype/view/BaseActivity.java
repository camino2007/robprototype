package fup.prototype.robprototype.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import fup.prototype.robprototype.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
            initContent();
      }

    private void initContent() {
        final BaseFragment baseFragment = createInitialContentFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, baseFragment);
        transaction.commit();
    }

    protected abstract int getLayoutId();

    protected abstract BaseFragment createInitialContentFragment();

}
