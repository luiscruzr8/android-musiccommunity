package com.tfm.musiccommunityapp.api.di

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.api.AuthApi
import com.tfm.musiccommunityapp.api.CitiesApi
import com.tfm.musiccommunityapp.api.PostsApi
import com.tfm.musiccommunityapp.api.RecommendationsApi
import com.tfm.musiccommunityapp.api.ScoresApi
import com.tfm.musiccommunityapp.api.TagsApi
import com.tfm.musiccommunityapp.api.UsersApi
import com.tfm.musiccommunityapp.api.network.NetworkDatasourceModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val networkDatasourceModule = NetworkDatasourceModule(
    apiServiceUrl = BuildConfig.BACKEND_URL,
    isForTesting = false
).networkModule

val apiModules = module {

    single {
        get<Retrofit>(named(NetworkDatasourceModule.SERVICE_RETROFIT)).create(AuthApi::class.java)
    }

    single {
        get<Retrofit>(named(NetworkDatasourceModule.SERVICE_RETROFIT)).create(UsersApi::class.java)
    }

    single {
        get<Retrofit>(named(NetworkDatasourceModule.SERVICE_RETROFIT)).create(TagsApi::class.java)
    }

    single {
        get<Retrofit>(named(NetworkDatasourceModule.SERVICE_RETROFIT)).create(PostsApi::class.java)
    }

    single {
        get<Retrofit>(named(NetworkDatasourceModule.SERVICE_RETROFIT)).create(CitiesApi::class.java)
    }

    single {
        get<Retrofit>(named(NetworkDatasourceModule.SERVICE_RETROFIT)).create(RecommendationsApi::class.java)
    }

    single {
        get<Retrofit>(named(NetworkDatasourceModule.SERVICE_RETROFIT)).create(ScoresApi::class.java)
    }

}