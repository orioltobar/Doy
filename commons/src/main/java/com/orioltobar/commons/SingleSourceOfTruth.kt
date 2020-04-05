package com.orioltobar.commons

/**
 * Single source of truth pattern. Database serves as SSOT in this case. Network calls are used
 * to store the values in the database.
 */
suspend fun <V, E> singleSourceOfTruth(
    dbDataSource: suspend (Unit) -> Response<V, E>,
    networkDataSource: suspend (Unit) -> Response<V, E>,
    dbCallback: suspend (V) -> Response<V, E>
): Response<V, E> =
    dbDataSource(Unit).either(onSuccess = { Success(it) },
        onFailure = {
            networkDataSource(Unit).either(
                onSuccess = { apiResult ->
                    dbCallback.invoke(apiResult)
                },
                onFailure = { error -> Failure(error) })
        })