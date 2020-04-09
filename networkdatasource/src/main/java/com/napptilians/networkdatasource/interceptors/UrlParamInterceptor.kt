package com.napptilians.networkdatasource.interceptors

import com.napptilians.networkdatasource.providers.NetworkProvider
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class UrlParamInterceptor @Inject constructor(
    private val networkProvider: NetworkProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newUrl = request.url().newBuilder().apply {
            // TODO: Add mandatory query params here.
            addQueryParameter("Example_Key", networkProvider.valueToBeProvided)
        }
        val newRequest = request.newBuilder().url(newUrl.build()).build()
        return chain.proceed(newRequest)
    }
}