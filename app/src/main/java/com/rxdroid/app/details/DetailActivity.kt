package com.rxdroid.app.details

import android.content.Context
import android.content.Intent
import com.rxdroid.repository.model.User
import com.rxdroid.app.R
import com.rxdroid.app.view.base.BaseActivity
import com.rxdroid.app.view.base.fragments.BaseFragment

class DetailActivity : BaseActivity() {

    object DetailConstants {
        const val KEY_USER: String = "keyUser"
    }

    companion object {
        fun createIntent(context: Context, user: User): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailConstants.KEY_USER, user)
            return intent
        }
    }

    override fun createInitialContentFragment(): BaseFragment<*> {
        val user: User = intent.getSerializableExtra(DetailConstants.KEY_USER) as User
        return DetailFragment.newInstance(user)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}