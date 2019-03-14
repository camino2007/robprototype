package com.rxdroid.app.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.User

class UserItemViewModel(private val user: User, private val clickListener: (ClickAction) -> Unit) : ViewModel(), ItemViewType {

    val userLogin: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()
    val repoCounter: MutableLiveData<String> = MutableLiveData()
    val avatarUrl: MutableLiveData<String> = MutableLiveData()

    override fun getItemViewType(): Int = AdapterConstants.USER_ITEM

    fun onClick() {
        clickListener(ClickAction.OnUser(user))
    }

}
