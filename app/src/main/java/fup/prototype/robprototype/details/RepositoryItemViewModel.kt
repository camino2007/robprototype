package fup.prototype.robprototype.details

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Repository

class RepositoryItemViewModel(private val repository: Repository?) : ViewModel(), ItemViewType {

    val repoName: MutableLiveData<String> = MutableLiveData()

    override fun getItemViewType(): Int {
        return AdapterConstants.REPOSITORY_ITEM
    }

    fun getRepository(): Repository? {
        return repository
    }

}