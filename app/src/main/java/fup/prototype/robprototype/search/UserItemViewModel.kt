package fup.prototype.robprototype.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rxdroid.repository.model.User

class UserItemViewModel(u: User) : ViewModel() {

    val loginName: MutableLiveData<String> = MutableLiveData()
    val repoCounter: MutableLiveData<String> = MutableLiveData()

    private val user: User = u

    fun getUser(): User {
        return user
    }

}