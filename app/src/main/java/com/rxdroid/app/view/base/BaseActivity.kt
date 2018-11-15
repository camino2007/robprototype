package com.rxdroid.app.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.rxdroid.app.R
import com.rxdroid.app.view.base.fragments.BaseFragment

abstract class BaseActivity : AppCompatActivity() {

    private object Constants {
        const val KEY_FRAGMENT = "keyFragment"
    }

    private var baseFragment: BaseFragment<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        baseFragment = getOrCreateFragment(savedInstanceState)
        initContent()
    }

    private fun getOrCreateFragment(savedInstanceState: Bundle?): BaseFragment<*> {
        savedInstanceState?.let {
            val keyFragment = savedInstanceState.getString(Constants.KEY_FRAGMENT)!!
            return supportFragmentManager.getFragment(savedInstanceState, keyFragment) as BaseFragment<*>
        }
        return createInitialContentFragment()
    }

    private fun initContent() {
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container, baseFragment as Fragment).commit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            val keyFragment = baseFragment?.getKey()!!
            outState.putString(Constants.KEY_FRAGMENT, keyFragment)
            supportFragmentManager.putFragment(outState, keyFragment, baseFragment as Fragment)
        }
    }

    protected abstract fun createInitialContentFragment(): BaseFragment<*>

    protected abstract fun getLayoutId(): Int
}