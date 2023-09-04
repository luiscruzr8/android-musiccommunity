package com.tfm.musiccommunityapp.ui.di

import com.tfm.musiccommunityapp.ui.utils.FileHelperImplementation
import com.tfm.musiccommunityapp.usecase.utils.FileHelper
import org.koin.dsl.bind
import org.koin.dsl.module

val uiModule = module {

    single {
        FileHelperImplementation(
            context = get()
        )
    } bind FileHelper::class

}