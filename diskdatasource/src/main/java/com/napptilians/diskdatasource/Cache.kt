package com.napptilians.diskdatasource

import com.napptilians.commons.Constants
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import java.util.*
import java.util.concurrent.TimeUnit

object Cache {

    /**
     * Simple cache system. It checks the current time and the [timestamp] from the [model] to see
     * whether is lower than the cache default time or not.
     *
     * @return Success if the value is not expired, failure otherwise.
     */
    fun <T> checkTimestampCache(timestamp: Long, model: T): Response<T, ErrorModel> = model?.let {
        if (Date().time - timestamp < TimeUnit.MINUTES.toMillis(Constants.CACHE_TIME_MINUTES)) {
            Success(it as T)
        } else {
            Failure(ErrorModel("cache error"))
        }
    } ?: run { Failure(ErrorModel("cache error")) }
}
