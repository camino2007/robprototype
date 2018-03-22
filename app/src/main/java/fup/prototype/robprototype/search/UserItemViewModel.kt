package fup.prototype.robprototype.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.User

class UserItemViewModel(u: User?) : ViewModel(), ItemViewType {

    val loginName: MutableLiveData<String> = MutableLiveData()
    val repoCounter: MutableLiveData<String> = MutableLiveData()

    private val user: User? = u

    fun getUser(): User? {
        return user
    }

    override fun getItemViewType(): Int {
        return AdapterConstants.USER_ITEM
    }

    fun onClick(viewModel: UserItemViewModel) {
        /*  val context = constraintLayout.context
          val user = viewModel.getUser()
          val intent = DetailsActivity.createIntent(context, user)
          context.startActivity(intent)*/
    }


}