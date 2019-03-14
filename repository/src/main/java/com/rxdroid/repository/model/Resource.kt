package com.rxdroid.repository.model

import com.rxdroid.api.error.RequestError

class Resource<out T>(val status: Status, val data: T?, val requestError: RequestError?) {

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(requestError: RequestError): Resource<T> {
            return Resource(Status.ERROR, null, requestError)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}
