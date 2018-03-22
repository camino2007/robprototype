package fup.prototype.robprototype.view

import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.User
import fup.prototype.robprototype.details.RepositoryItemViewModel
import fup.prototype.robprototype.search.UserItemViewModel

class ItemViewModelFactory {

    companion object {
        fun create(user: User?): ItemViewType? {
            user?.let {
                val viewModel = UserItemViewModel(user)
                viewModel.loginName.postValue(user.login)
                viewModel.repoCounter.postValue(user.publicRepoCount.toString())
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