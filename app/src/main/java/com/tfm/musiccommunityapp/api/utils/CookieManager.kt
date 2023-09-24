package com.tfm.musiccommunityapp.api.utils

import com.tfm.musiccommunityapp.data.extensions.isSignInRequest
import com.tfm.musiccommunityapp.data.extensions.isSignOutRequest
import com.tfm.musiccommunityapp.data.extensions.server
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManager() : CookieJar {

    private val cookieMap: MutableMap<String, List<Cookie>> = mutableMapOf()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        if (url.isSignOutRequest){
            cookieMap.clear()
        }
        return cookieMap[url.server] ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if(url.isSignInRequest && cookies.isNotEmpty()) {
            removeCookiesSession(url)
            cookieMap[url.server] = cookies
        }
    }

    fun removeCookiesSession(url: HttpUrl) {
        cookieMap.remove(url.server)
    }

}