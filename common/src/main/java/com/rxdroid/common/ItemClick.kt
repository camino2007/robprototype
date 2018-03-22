package com.rxdroid.common

interface ItemClick<in T> {

    fun onClick(t: T)
}