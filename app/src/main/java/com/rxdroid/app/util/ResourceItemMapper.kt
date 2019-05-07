package com.rxdroid.app.util

import com.rxdroid.repository.model.Resource
import io.reactivex.ObservableTransformer

interface ResourceItemMapper<Source, Result> {

    fun map(): ObservableTransformer<Resource<Source>, Resource<Result>>

}
