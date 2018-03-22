package fup.prototype.robprototype.details

import android.content.Context
import android.content.Intent
import com.rxdroid.repository.model.User
import fup.prototype.robprototype.R
import fup.prototype.robprototype.view.base.BaseActivity
import fup.prototype.robprototype.view.base.fragments.BaseFragment

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

    override fun createInitialContentFragment(): BaseFragment<*, *> {
        val user: User = intent.getSerializableExtra(DetailConstants.KEY_USER) as User
        return DetailFragment.newInstance(user)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}