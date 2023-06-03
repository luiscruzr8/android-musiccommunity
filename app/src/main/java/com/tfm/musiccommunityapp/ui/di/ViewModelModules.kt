package com.tfm.musiccommunityapp.ui.di

import com.tfm.musiccommunityapp.ui.home.HomeScreenViewModel
import com.tfm.musiccommunityapp.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeScreenViewModel()
    }

    viewModel {
        LoginViewModel(
            signInUseCase = get(),
            signUpUseCase = get()
        )
    }

}