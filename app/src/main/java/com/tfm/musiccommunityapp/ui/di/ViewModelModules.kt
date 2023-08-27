package com.tfm.musiccommunityapp.ui.di

import com.tfm.musiccommunityapp.ui.community.advertisements.AdvertisementsViewModel
import com.tfm.musiccommunityapp.ui.community.advertisements.detail.AdvertisementDetailViewModel
import com.tfm.musiccommunityapp.ui.community.discussions.DiscussionsViewModel
import com.tfm.musiccommunityapp.ui.community.discussions.detail.DiscussionDetailViewModel
import com.tfm.musiccommunityapp.ui.community.events.EventsViewModel
import com.tfm.musiccommunityapp.ui.community.events.detail.EventDetailViewModel
import com.tfm.musiccommunityapp.ui.community.opinions.OpinionsViewModel
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

    viewModel {
        EventsViewModel(
            getEvents = get(),
            dispatcher = get()
        )
    }

    viewModel {
        AdvertisementsViewModel(
            getAdvertisements = get(),
            dispatcher = get()
        )
    }

    viewModel {
        DiscussionsViewModel(
            getDiscussions = get(),
            dispatcher = get()
        )
    }

    viewModel {
        OpinionsViewModel(
            getOpinions = get(),
            dispatcher = get()
        )
    }

    viewModel {
        EventDetailViewModel(
            getEventById = get(),
            getPostImageByPostId = get(),
            dispatcher = get()
        )
    }

    viewModel {
        AdvertisementDetailViewModel(
            getAdvertisementById = get(),
            getPostImageByPostId = get(),
            dispatcher = get()
        )
    }

    viewModel {
        DiscussionDetailViewModel(
            getDiscussionById = get(),
            getPostImageByPostId = get(),
            dispatcher = get()
        )
    }

    /*viewModel {
        OpinionDetailViewModel(
            getDiscussionById = get(),
            dispatcher = get()
        )
    }
    */

}