package fup.prototype.robprototype.view.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import fup.prototype.robprototype.R
import fup.prototype.robprototype.view.base.fragments.BaseFragment
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private object Constants {
        const val KEY_FRAGMENT = "keyFragment"
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private var baseFragment: BaseFragment<*, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        baseFragment = getOrCreateFragment(savedInstanceState)
        initContent()
    }

    private fun getOrCreateFragment(savedInstanceState: Bundle?): BaseFragment<*, *> {
        savedInstanceState?.let {
            val keyFragment = savedInstanceState.getString(Constants.KEY_FRAGMENT)
            return supportFragmentManager.getFragment(savedInstanceState, keyFragment) as BaseFragment<*, *>
        }
        return createInitialContentFragment()
    }

    private fun initContent() {
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container, baseFragment).commit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            outState.putString(Constants.KEY_FRAGMENT, baseFragment?.getKey())
            supportFragmentManager.putFragment(outState, baseFragment?.getKey(), baseFragment)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    protected abstract fun createInitialContentFragment(): BaseFragment<*, *>

    protected abstract fun getLayoutId(): Int
}