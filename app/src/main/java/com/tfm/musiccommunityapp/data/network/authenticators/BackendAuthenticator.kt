package com.tfm.musiccommunityapp.data.network.authenticators

import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import com.tfm.musiccommunityapp.data.network.di.OkHttpClientProvider
import com.tfm.musiccommunityapp.data.extensions.isSignInRequest
import com.tfm.musiccommunityapp.data.extensions.server
import com.tfm.musiccommunityapp.data.utils.CookieManager
import okhttp3.Authenticator
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response

internal class BackendAuthenticator(private val localAuth: AuthDatasource, private val cookieManager: CookieManager):
    Authenticator {

    companion object {
        const val HEADER_AUTH = "Authorization"
    }

    private val client = OkHttpClientProvider.defaultOkHttp.newBuilder().apply { cookieJar(cookieManager) }.build()

    private val HttpUrl.baseUrl: String
        get() = toString().let { url -> url.subSequence(0, url.lastIndexOf(server)).toString() }

    private fun refreshRequest(response: Response): Request? {
        val request = response.request

        cookieManager.removeCookiesSession(request.url)

        return synchronized(this) {
            val urlCredentials = "${request.url.baseUrl}${request.url.server}/signin"
            val credentials = localAuth.bearerToken ?: ""

            val requestLogin = Request.Builder()
                .url(urlCredentials)
                .header(HEADER_AUTH, credentials)
                .build()

            val responseLogin = client.newCall(requestLogin).execute()

            if(responseLogin.isSuccessful) {
                response.request.newBuilder().addHeader(HEADER_AUTH, credentials).build()
            } else {
                null
            }
        }
    }

    override fun authenticate(route: okhttp3.Route?, response: Response): Request? {
        val request = response.request
        return when {
            request.url.isSignInRequest -> null
            else -> refreshRequest(response)
        }
    }

}