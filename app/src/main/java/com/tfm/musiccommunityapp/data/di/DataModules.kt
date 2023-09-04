package com.tfm.musiccommunityapp.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tfm.musiccommunityapp.data.datasource.AdvertisementDatasource
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import com.tfm.musiccommunityapp.data.datasource.CityDatasource
import com.tfm.musiccommunityapp.data.datasource.DiscussionDatasource
import com.tfm.musiccommunityapp.data.datasource.EventDatasource
import com.tfm.musiccommunityapp.data.datasource.GenericPostDatasource
import com.tfm.musiccommunityapp.data.datasource.LoginDatasource
import com.tfm.musiccommunityapp.data.datasource.OpinionDatasource
import com.tfm.musiccommunityapp.data.datasource.RecommendationDatasource
import com.tfm.musiccommunityapp.data.datasource.ScoreDatasource
import com.tfm.musiccommunityapp.data.datasource.TagDatasource
import com.tfm.musiccommunityapp.data.datasource.UserDatasource
import com.tfm.musiccommunityapp.data.datasource.preferences.SharedPreferencesAuthImpl
import com.tfm.musiccommunityapp.data.datasource.remote.AdvertisementRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.CityRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.DiscussionRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.EventRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.GenericPostRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.LoginRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.OpinionRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.RecommendationRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.ScoreRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.TagRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.datasource.remote.UserRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.repository.AdvertisementRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.AuthRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.CityRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.CommonPostRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.DiscussionRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.EventRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.OpinionRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.RecommendationRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.ScoreRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.TagRepositoryImpl
import com.tfm.musiccommunityapp.data.repository.UserProfileRepositoryImpl
import com.tfm.musiccommunityapp.usecase.repository.AdvertisementRepository
import com.tfm.musiccommunityapp.usecase.repository.AuthRepository
import com.tfm.musiccommunityapp.usecase.repository.CityRepository
import com.tfm.musiccommunityapp.usecase.repository.CommonPostRepository
import com.tfm.musiccommunityapp.usecase.repository.DiscussionRepository
import com.tfm.musiccommunityapp.usecase.repository.EventRepository
import com.tfm.musiccommunityapp.usecase.repository.OpinionRepository
import com.tfm.musiccommunityapp.usecase.repository.RecommendationRepository
import com.tfm.musiccommunityapp.usecase.repository.ScoreRepository
import com.tfm.musiccommunityapp.usecase.repository.TagRepository
import com.tfm.musiccommunityapp.usecase.repository.UserProfileRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteDatasourceModules = module {

    single {
        LoginRemoteDatasourceImpl(
            authApi = get()
        )
    } bind LoginDatasource::class

    single {
        UserRemoteDatasourceImpl(
            usersApi = get()
        )
    } bind UserDatasource::class

    single {
        TagRemoteDatasourceImpl(
            tagsApi = get()
        )
    } bind TagDatasource::class

    single {
        GenericPostRemoteDatasourceImpl(
            postsApi = get()
        )
    } bind GenericPostDatasource::class

    single {
        AdvertisementRemoteDatasourceImpl(
            advertisementApi = get()
        )
    } bind AdvertisementDatasource::class

    single {
        DiscussionRemoteDatasourceImpl(
            discussionApi = get()
        )
    } bind DiscussionDatasource::class

    single {
        EventRemoteDatasourceImpl(
            eventApi = get()
        )
    } bind EventDatasource::class

    single {
        OpinionRemoteDatasourceImpl(
            opinionApi = get()
        )
    } bind OpinionDatasource::class

    single {
        CityRemoteDatasourceImpl(
            cityApi = get()
        )
    } bind CityDatasource::class

    single {
        RecommendationRemoteDatasourceImpl(
            recommendationApi = get()
        )
    } bind RecommendationDatasource::class

    single {
        ScoreRemoteDatasourceImpl(
            scoresApi = get(),
            fileHelper = get()
        )
    } bind ScoreDatasource::class
}

val localDatasourceModule = module {

    single {
        SharedPreferencesAuthImpl(
            sharedPreferences = get(),
            encryptedPreferences = get()
        )
    } bind AuthDatasource::class

}

val repositoriesModule = module {

    single {
        AuthRepositoryImpl(
            authDatasource = get(),
            loginRemoteDatasource = get(),
            cache = get()
        )
    } bind AuthRepository::class

    single {
        UserProfileRepositoryImpl(
            userDatasource = get(),
        )
    } bind UserProfileRepository::class

    single {
        TagRepositoryImpl(
            tagDatasource = get()
        )
    } bind TagRepository::class

    single {
        CommonPostRepositoryImpl(
            commonDatasource = get()
        )
    } bind CommonPostRepository::class

    single {
        EventRepositoryImpl(
            eventDatasource = get()
        )
    } bind EventRepository::class

    single {
        AdvertisementRepositoryImpl(
            advertisementDatasource = get()
        )
    } bind AdvertisementRepository::class

    single {
        DiscussionRepositoryImpl(
            discussionDatasource = get()
        )
    } bind DiscussionRepository::class

    single {
        OpinionRepositoryImpl(
            opinionDatasource = get()
        )
    } bind OpinionRepository::class

    single {
        CityRepositoryImpl(
            cityDatasource = get()
        )
    } bind CityRepository::class

    single {
        RecommendationRepositoryImpl(
            recommendationDatasource = get()
        )
    } bind RecommendationRepository::class

    single {
        ScoreRepositoryImpl(
            scoreDatasource = get()
        )
    } bind ScoreRepository::class
    
}

val preferencesModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "MuComSharedPreferences",
            Context.MODE_PRIVATE
        )
    }

    single { provideEncryptedPreferences(get()) }
}

private fun provideEncryptedPreferences(context: Context): EncryptedSharedPreferences =
    EncryptedSharedPreferences.create(
        "MuComEncryptedSharedPreferences",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    ) as EncryptedSharedPreferences
