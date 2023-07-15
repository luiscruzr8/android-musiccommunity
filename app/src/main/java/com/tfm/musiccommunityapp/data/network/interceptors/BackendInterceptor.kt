package com.tfm.musiccommunityapp.data.network.interceptors

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import okhttp3.Interceptor
import okhttp3.Response

internal class BackendInterceptor(private val localAuth: AuthDatasource): Interceptor {

    companion object {
        const val HEADER_AUTH = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val authUrl = chain.request().url.let { url ->
            "${BuildConfig.BACKEND_URL}auth/signin"
        }

        val request = chain.request()

        return when(request.url.toString()) {
            authUrl -> chain.proceed(request) //Ignore auth requests, they are treated in repository
            else -> handleRequest(chain)
            }
        }

    private fun handleRequest(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader(
                HEADER_AUTH,
                localAuth.bearerToken
            )
        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}