package com.tfm.musiccommunityapp.ui.base

import android.app.Application
import com.tfm.musiccommunityapp.api.di.apiModules
import com.tfm.musiccommunityapp.api.di.networkDatasourceModule
import com.tfm.musiccommunityapp.data.di.localDatasourceModule
import com.tfm.musiccommunityapp.data.di.preferencesModule
import com.tfm.musiccommunityapp.data.di.remoteDatasourceModules
import com.tfm.musiccommunityapp.data.di.repositoriesModule
import com.tfm.musiccommunityapp.ui.di.uiModule
import com.tfm.musiccommunityapp.ui.di.viewModelModule
import com.tfm.musiccommunityapp.usecase.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin

class MusicCommunityApp : Application() {

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MusicCommunityApp.applicationContext)
            modules(
                listOf(
                    networkDatasourceModule,
                    apiModules,
                    remoteDatasourceModules,
                    localDatasourceModule,
                    repositoriesModule,
                    useCaseModule,
                    viewModelModule,
                    uiModule,
                    preferencesModule
                )
            )
        }
    }
}