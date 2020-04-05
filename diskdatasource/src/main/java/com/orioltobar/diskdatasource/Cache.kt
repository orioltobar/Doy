package com.orioltobar.diskdatasource

import com.orioltobar.commons.Constants
import com.orioltobar.commons.Failure
import com.orioltobar.commons.Response
import com.orioltobar.commons.Success
import com.orioltobar.commons.error.ErrorModel
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
            Success(it)
        } else {
            Failure(ErrorModel(""))
        }
    } ?: run { Failure(ErrorModel("")) }
}
