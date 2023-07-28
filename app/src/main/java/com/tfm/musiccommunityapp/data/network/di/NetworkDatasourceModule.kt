package com.tfm.musiccommunityapp.data.network.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tfm.musiccommunityapp.data.clients.ApiOkHttpClient
import com.tfm.musiccommunityapp.data.clients.RetrofitOkHttpClient
import com.tfm.musiccommunityapp.data.network.authenticators.BackendAuthenticator
import com.tfm.musiccommunityapp.data.utils.CookieManager
import com.tfm.musiccommunityapp.data.utils.LocalDateTimeJsonAdapter
import com.tfm.musiccommunityapp.data.utils.NetworkTrackerImpl
import okhttp3.Authenticator
import okhttp3.Cache
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import java.time.LocalDateTime

class NetworkDatasourceModule(
    apiServiceUrl: String,
    isForTesting: Boolean = false
) {
    companion object {
        const val SERVICE_OKHTTP_CLIENT = "service_okhttp_client"
        const val SERVICE_RETROFIT = "service_retrofit"
    }

    val networkModule = module {

        single <Gson> {
            GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeJsonAdapter())
                .create()
        }

        single {
            CookieManager()
        }

        single {
            provideCache(
                context = get()
            )
        }

        single(named(SERVICE_RETROFIT)) {
            RetrofitOkHttpClient(
                baseUrl = apiServiceUrl,
                baseOkHttpClient = get(named(SERVICE_OKHTTP_CLIENT)),
                gson = get()
            ).build()
        }


        single(named(SERVICE_OKHTTP_CLIENT)) {
            ApiOkHttpClient(
                authenticator = get(),
                isForTesting = isForTesting,
                localAuth = get(),
                cookieManager = get(),
                cache = get()
            ).build()
        }

        single {
            BackendAuthenticator(
                localAuth = get(),
                cookieManager = get()
            )
        } bind Authenticator::class

        single {
            NetworkTrackerImpl(
                context = get()
            )
        }
    }

    private fun provideCache(context: Context) = Cache(context.cacheDir, 10 * 1024 * 1024)
}