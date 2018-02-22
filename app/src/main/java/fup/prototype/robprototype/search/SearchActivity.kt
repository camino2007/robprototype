package fup.prototype.robprototype.search

import fup.prototype.robprototype.R
import fup.prototype.robprototype.view.base.BaseActivity

class SearchActivity : BaseActivity() {

    override fun createInitialContentFragment(): SearchFragment {
        return SearchFragment.newInstance()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}