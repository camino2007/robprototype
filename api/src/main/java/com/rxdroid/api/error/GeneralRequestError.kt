package com.rxdroid.api.error

import retrofit2.Response

class GeneralRequestError(response: Response<*>?, throwable: Throwable?)
    : RequestError(response, throwable, -1)
