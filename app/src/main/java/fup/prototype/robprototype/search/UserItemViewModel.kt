package fup.prototype.robprototype.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.ItemClick
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.User

class UserItemViewModel(private val user: User) : ViewModel(), ItemViewType, ItemClick<UserItemViewModel> {

    val loginName: MutableLiveData<String> = MutableLiveData()
    val repoCounter: MutableLiveData<String> = MutableLiveData()

    private val publishClick: PublishRelay<User> = PublishRelay.create()

    override fun getItemViewType(): Int {
        return AdapterConstants.USER_ITEM
    }

    override fun onClick(t: UserItemViewModel) {
        publishClick.accept(t.user)
    }

    fun getClickSubject(): PublishRelay<User> {
        return publishClick
    }

}