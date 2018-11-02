package com.rxdroid.app.view.base

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.rxdroid.app.view.base.fragments.BaseFragment
import com.rxdroid.app.R

abstract class BaseActivity : AppCompatActivity() {

    private object Constants {
        const val KEY_FRAGMENT = "keyFragment"
    }

    private var baseFragment: BaseFragment<*, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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

    protected abstract fun createInitialContentFragment(): BaseFragment<*, *>

    protected abstract fun getLayoutId(): Int
}