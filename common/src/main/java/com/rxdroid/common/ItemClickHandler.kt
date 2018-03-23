package com.rxdroid.common

interface ItemClickHandler<in T> {

    fun onClick(t: T)
}