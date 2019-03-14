package com.rxdroid.app.view

import com.rxdroid.app.details.RepositoryItemViewModel
import com.rxdroid.app.search.ClickAction
import com.rxdroid.app.search.UserItemViewModel
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.User

class ItemViewModelFactory {

    companion object {
        fun create(user: User?, clickListener: (ClickAction) -> Unit): ItemViewType? {
            return user?.let {
                val itemViewModel = UserItemViewModel(it, clickListener)
                itemViewModel.userLogin.postValue(it.login)
                itemViewModel.userName.postValue(it.name)
                itemViewModel.repoCounter.postValue(it.publicRepoCount.toString())
                itemViewModel.avatarUrl.postValue(it.avatarUrl)
                return itemViewModel
            }
        }

        fun create(repository: Repository?): ItemViewType? {
            return repository?.let {
                val viewModel = RepositoryItemViewModel(it)
                viewModel.repoName.postValue(it.name)
                return viewModel
            }
        }

        fun create(repositories: List<Repository>?): List<ItemViewType> {
            repositories?.let {
                val viewTypes: ArrayList<ItemViewType> = ArrayList()
                var viewType: ItemViewType?
                for (repository in repositories) {
                    viewType = create(repository)
                    if (viewType != null) {
                        viewTypes.add(viewType)
                    }
                }
                return viewTypes
            }
            return emptyList()
        }

    }

}