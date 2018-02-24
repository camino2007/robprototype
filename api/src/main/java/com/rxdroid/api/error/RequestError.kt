package com.rxdroid.api.error

import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

abstract class RequestError(val response: Response<*>?,
                            val throwable: Throwable?,
                            val errorCode: Int) {

    companion object {

        val ERROR_CODE_NO_SEARCH_INPUT = 102
        val ERROR_CODE_NO_RESULTS = 103

        fun create(response: Response<*>?, throwable: Throwable?): RequestError {
            if (throwable != null) {
                if (throwable is ConnectException || throwable is UnknownHostException) {
                    return NoConnectionRequestError(throwable)
                }
            }
            return GeneralRequestError(response, throwable)
        }

        fun create(errorCode: Int): RequestError {
            return CustomRequestError(errorCode)
        }

    }

}