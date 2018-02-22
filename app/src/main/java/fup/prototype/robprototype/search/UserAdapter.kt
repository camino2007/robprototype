package fup.prototype.robprototype.search

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rxdroid.repository.model.User
import fup.prototype.robprototype.R
import fup.prototype.robprototype.databinding.ItemUserBinding
import fup.prototype.robprototype.view.ItemViewModelFactory
import fup.prototype.robprototype.view.base.adapters.BaseRecyclerAdapter

class UserAdapter : BaseRecyclerAdapter<User, ItemUserBinding>() {

    override fun createBinding(parent: ViewGroup?): ItemUserBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.item_user, parent, false)
    }

    override fun bind(binding: ItemUserBinding?, item: User) {
        val userItemViewModel = ItemViewModelFactory.create(item)
        binding?.viewModel = userItemViewModel
        binding?.handler = UserItemHandler()
    }

}