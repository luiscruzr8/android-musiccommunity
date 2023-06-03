package com.tfm.musiccommunityapp.data.clients

import android.content.Context
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import com.tfm.musiccommunityapp.data.network.di.OkHttpClientProvider
import com.tfm.musiccommunityapp.data.network.authenticators.BackendAuthenticator
import com.tfm.musiccommunityapp.data.utils.CookieManager
import okhttp3.Cache
import okhttp3.CookieJar
import okhttp3.OkHttpClient

class ApiOkHttpClient(
    private val context: Context,
    private val isForTesting: Boolean = false,
    private val localAuth: AuthDatasource,
    private val cookieManager: CookieManager,
    private val cache: Cache
) {

    fun build(): OkHttpClient = OkHttpClientProvider.build(
        authenticator = BackendAuthenticator(
            localAuth = localAuth,
            cookieManager = cookieManager
        ),
        networkInterceptor = null,
        interceptors = emptyList(),
        cookieJar = cookieManager as CookieJar,
        cache = cache,
        isForTesting = isForTesting
    )

}