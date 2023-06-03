package com.tfm.musiccommunityapp.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.AuthApi
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import com.tfm.musiccommunityapp.data.datasource.LoginDatasource
import com.tfm.musiccommunityapp.data.datasource.preferences.SharedPreferencesAuthImpl
import com.tfm.musiccommunityapp.data.datasource.remote.LoginRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.network.di.NetworkDatasourceModule
import com.tfm.musiccommunityapp.data.repository.AuthRepositoryImpl
import com.tfm.musiccommunityapp.domain.repository.AuthRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.bind
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
}

val remoteDatasourceModules = module {

    single {
        LoginRemoteDatasourceImpl(
            authApi = get()
        )
    } bind LoginDatasource::class

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
