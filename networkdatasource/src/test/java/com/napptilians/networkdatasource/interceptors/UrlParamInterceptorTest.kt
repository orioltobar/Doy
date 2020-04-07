package com.napptilians.networkdatasource.interceptors

import com.napptilians.networkdatasource.providers.NetworkProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test

class UrlParamInterceptorTest {

    init {
        MockKAnnotations.init(this, relaxed = true)
    }

    @MockK
    private lateinit var networkProviderMock: NetworkProvider

    @MockK
    private lateinit var chain: Interceptor.Chain
    @MockK
    private lateinit var request: Request
    @MockK
    private lateinit var httpUrl: HttpUrl
    @MockK
    private lateinit var httpUrlBuilder: HttpUrl.Builder
    @MockK
    private lateinit var requestBuilder: Request.Builder
    @MockK
    private lateinit var response: Response

    private val baseUrlInterceptor by lazy {
        UrlParamInterceptor(networkProviderMock)
    }

    @Before
    fun setup() {
        every { chain.request() } returns request
        every { request.url() } returns httpUrl
        every { httpUrl.pathSegments() } returns emptyList()
        every { httpUrl.newBuilder() } returns httpUrlBuilder
        every { httpUrlBuilder.scheme(any()) } returns httpUrlBuilder
        every { httpUrlBuilder.host(any()) } returns httpUrlBuilder
        every { httpUrlBuilder.setPathSegment(any(), any()) } returns httpUrlBuilder
        every { httpUrlBuilder.addPathSegment(any()) } returns httpUrlBuilder
        every { httpUrlBuilder.build() } returns httpUrl

        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.url(httpUrl) } returns requestBuilder
        every { requestBuilder.build() } returns request

        every { chain.proceed(request) } returns response
    }

    @Test
    fun `URL is well formed when params are not empty`() {
        every { networkProviderMock.valueToBeProvided } returns "test"

        baseUrlInterceptor.intercept(chain)

        verifySequence {
            httpUrlBuilder.addQueryParameter(any(), "test")
            httpUrlBuilder.addQueryParameter(any(), "en")
            httpUrlBuilder.build()
        }
    }

    @Test
    fun `URL is well formed when params are empty`() {
        every { networkProviderMock.valueToBeProvided } returns ""

        baseUrlInterceptor.intercept(chain)

        verifySequence {
            httpUrlBuilder.addQueryParameter(any(), "")
            httpUrlBuilder.addQueryParameter(any(), "")
            httpUrlBuilder.build()
        }
    }
}