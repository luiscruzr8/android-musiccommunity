package com.tfm.musiccommunityapp.data.network.di

import com.tfm.musiccommunityapp.BuildConfig
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.CookieJar
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClientProvider {

    val defaultOkHttp: OkHttpClient  = build(null, null, emptyList(), null, null, false)

    fun build(
        authenticator: Authenticator?,
        networkInterceptor: Interceptor?,
        interceptors: List<Interceptor>,
        cookieJar: CookieJar?,
        cache: Cache?,
        isForTesting: Boolean
    ): OkHttpClient = OkHttpClient.Builder().apply {
        connectionPool(ConnectionPool(10, 20, TimeUnit.MINUTES))
        dispatcher(Dispatcher().apply {
            maxRequests = 50*5
            maxRequestsPerHost = 20
        })

        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(40, TimeUnit.SECONDS)

        cookieJar?.let { cookieJar(cookieJar) }

        authenticator?.let { authenticator(authenticator) }

        if(!isForTesting) {
            cache(cache)
            networkInterceptor?.let { addNetworkInterceptor(networkInterceptor) }
            interceptors() += interceptors
        }

        if(BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
        }

    }.build()
}