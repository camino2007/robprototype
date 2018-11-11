package com.rxdroid.app.view

import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.User
import com.rxdroid.app.details.RepositoryItemViewModel
import com.rxdroid.app.search.UserItemViewModel

class ItemViewModelFactory {

    companion object {
        fun create(user: User?, clickListener: (User) -> Unit): ItemViewType? {
            user?.let {
                val viewModel = UserItemViewModel(user, clickListener)
                viewModel.userLogin.postValue(user.login)
                viewModel.userName.postValue(user.name)
                viewModel.repoCounter.postValue(user.publicRepoCount.toString())
                viewModel.avatarUrl.postValue(user.avatarUrl)
                return viewModel
            }
            return null
        }

        fun create(repository: Repository?): ItemViewType? {
            repository?.let {
                val viewModel = RepositoryItemViewModel(repository)
                viewModel.repoName.postValue(repository.name)
                return viewModel
            }
            return null
        }

        fun create(repositories: List<Repository>?): ArrayList<ItemViewType> {
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
            return ArrayList()
        }

    }

}