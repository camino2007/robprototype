package com.rxdroid.common.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class DelegateAdapter : ViewTypeDelegateAdapter {

    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        layoutInflater.let {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater!!, getLayoutId(), parent, false)
        return BindingViewHolder(binding)
    }

    abstract fun getLayoutId(): Int

    private class BindingViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}