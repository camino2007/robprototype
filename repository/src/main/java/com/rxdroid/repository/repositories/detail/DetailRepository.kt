package com.rxdroid.repository.repositories.detail

import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import com.rxdroid.repository.repositories.UiRepository
import io.reactivex.Observable

interface DetailRepository : UiRepository<ArrayList<Repository>> {

    fun loadReposForUser(user: User): Observable<Resource<ArrayList<Repository>>>

}