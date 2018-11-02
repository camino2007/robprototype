package com.rxdroid.app.search

import com.rxdroid.app.R
import com.rxdroid.app.view.base.BaseActivity

class SearchActivity : BaseActivity() {

    override fun createInitialContentFragment(): SearchFragment {
        return SearchFragment.newInstance()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}