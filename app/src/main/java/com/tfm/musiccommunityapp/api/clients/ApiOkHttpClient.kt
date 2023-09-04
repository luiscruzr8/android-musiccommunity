package com.tfm.musiccommunityapp.api.clients

import com.tfm.musiccommunityapp.api.network.interceptors.BackendInterceptor
import com.tfm.musiccommunityapp.api.network.provider.OkHttpClientProvider
import com.tfm.musiccommunityapp.api.utils.CookieManager
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.CookieJar
import okhttp3.OkHttpClient

class ApiOkHttpClient(
    private val authenticator: Authenticator,
    private val isForTesting: Boolean = false,
    private val localAuth: AuthDatasource,
    private val cookieManager: CookieManager,
    private val cache: Cache
) {

    fun build(): OkHttpClient = OkHttpClientProvider.build(
        authenticator = authenticator,
        networkInterceptor = null,
        interceptors = listOf(
            BackendInterceptor(localAuth)
        ),
        cookieJar = cookieManager as CookieJar,
        cache = cache,
        isForTesting = isForTesting
    )

}