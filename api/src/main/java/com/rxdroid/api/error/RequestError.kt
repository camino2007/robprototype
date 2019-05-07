package com.rxdroid.api.error

import androidx.annotation.StringRes
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.UnknownHostException

sealed class RequestError {

    companion object {

        fun create(throwable: Throwable?): RequestError = when (throwable) {
            is ConnectException, is UnknownHostException -> NoConnection(throwable)
            else -> General(throwable)
        }

        fun create(@StringRes errorTitle: Int, @StringRes errorText: Int): RequestError = CustomText(errorTitle, errorText)

        fun create(response: Response<*>): RequestError {
            return if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                NoResults(response)
            } else {
                Server(response)
            }
        }

    }

    data class NoConnection(val throwable: Throwable?) : RequestError()

    data class General(val throwable: Throwable?) : RequestError()

    data class CustomText(@StringRes val errorTitle: Int, @StringRes val errorText: Int) : RequestError()

    data class NoResults(val response: Response<*>) : RequestError()

    data class Server(val response: Response<*>) : RequestError()

}
