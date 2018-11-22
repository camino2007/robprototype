package com.rxdroid.app.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Repository

class RepositoryItemViewModel(private val repository: Repository?) : ViewModel(), ItemViewType {

    val repoName: MutableLiveData<String> = MutableLiveData()

    override fun getItemViewType() =  AdapterConstants.REPOSITORY_ITEM

    fun getRepository(): Repository? = repository

}
