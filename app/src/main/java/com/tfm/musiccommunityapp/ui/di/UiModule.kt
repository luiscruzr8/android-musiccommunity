package com.tfm.musiccommunityapp.ui.di

import com.tfm.musiccommunityapp.domain.utils.FileHelper
import com.tfm.musiccommunityapp.utils.FileHelperImplementation
import org.koin.dsl.bind
import org.koin.dsl.module

val uiModule = module {

    single {
        FileHelperImplementation(
            context = get()
        )
    } bind FileHelper::class

}