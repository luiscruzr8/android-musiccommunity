package com.tfm.musiccommunityapp.ui.di

import com.tfm.musiccommunityapp.ui.community.users.UsersViewModel
import com.tfm.musiccommunityapp.ui.home.HomeScreenViewModel
import com.tfm.musiccommunityapp.ui.login.LoginViewModel
import com.tfm.musiccommunityapp.ui.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    single { Dispatchers.IO }

    viewModel {
        HomeScreenViewModel(
            getCurrentUser = get(),
            dispatcher = get()
        )
    }

    viewModel {
        LoginViewModel(
            signInUseCase = get(),
            signUpUseCase = get(),
            dispatcher = get()
        )
    }

    viewModel {
        ProfileViewModel(
            getUserInfo = get(),
            getUserFollowers = get(),
            getUserFollowing = get(),
            updateProfile = get(),
            signOut = get(),
            dispatcher = get()
        )
    }

    viewModel {
        UsersViewModel(
            getUsers = get(),
            dispatcher = get()
        )
    }

}