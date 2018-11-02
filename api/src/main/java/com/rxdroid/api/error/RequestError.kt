package com.rxdroid.api.error

import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

sealed class RequestError {

    companion object {

        const val ERROR_CODE_NO_SEARCH_INPUT = 102

        fun create(throwable: Throwable?): RequestError = when (throwable) {
            is ConnectException, is UnknownHostException -> NoConnectionRequestError(throwable)
            else -> GeneralRequestError(throwable)
        }

        fun create(errorCode: Int) = CustomRequestError(errorCode)

        fun create(response: Response<*>?) = ServerRequestError(response)
    }
}

data class NoConnectionRequestError(val throwable: Throwable?) : RequestError()

data class GeneralRequestError(val throwable: Throwable?) : RequestError()

data class CustomRequestError(val errorCode: Int) : RequestError()

data class ServerRequestError(val response: Response<*>?) : RequestError()
