package com.rxdroid.app.search

import com.rxdroid.app.R
import com.rxdroid.app.view.base.BaseActivity

class SearchActivity : BaseActivity() {

    override fun createInitialContentFragment() = SearchFragment.newInstance()

    override fun getLayoutId() = R.layout.activity_main

}
