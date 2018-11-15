package com.rxdroid.app.view


interface ViewProvider<in B> {

    fun getLayoutId(): Int

    fun getKey(): String

    fun addViewListener()

    fun initBinding(binding: B)

}

