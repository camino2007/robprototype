package com.rxdroid.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.rxdroid.common.ItemClickHandler

interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(clickHandler: ItemClickHandler<*>?, holder: RecyclerView.ViewHolder, item: ItemViewType)
}