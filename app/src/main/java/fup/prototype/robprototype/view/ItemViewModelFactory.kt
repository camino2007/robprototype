package fup.prototype.robprototype.view

import com.rxdroid.repository.model.User
import fup.prototype.robprototype.search.UserItemViewModel

class ItemViewModelFactory {

    companion object {
        fun create(user: User): UserItemViewModel {
            val viewModel = UserItemViewModel(user)
            viewModel.loginName.postValue(user.login)
            viewModel.repoCounter.postValue(user.publicRepoCount.toString())
            return viewModel
        }


        /*    fun create(repository: Repository): RepoItemViewModel {
                  final RepoItemViewModel viewModel = new RepoItemViewModel(repository);
                     viewModel.repoName.set(repository.getName());
                     return viewModel;
        }*/
    }

}