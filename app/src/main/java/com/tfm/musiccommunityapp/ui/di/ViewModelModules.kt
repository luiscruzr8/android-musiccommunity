package com.tfm.musiccommunityapp.ui.di

import com.tfm.musiccommunityapp.ui.community.CommunityViewModel
import com.tfm.musiccommunityapp.ui.community.advertisements.AdvertisementsViewModel
import com.tfm.musiccommunityapp.ui.community.advertisements.detail.AdvertisementDetailViewModel
import com.tfm.musiccommunityapp.ui.community.discussions.DiscussionsViewModel
import com.tfm.musiccommunityapp.ui.community.discussions.detail.DiscussionDetailViewModel
import com.tfm.musiccommunityapp.ui.community.events.EventsViewModel
import com.tfm.musiccommunityapp.ui.community.events.detail.EventDetailViewModel
import com.tfm.musiccommunityapp.ui.community.opinions.OpinionsViewModel
import com.tfm.musiccommunityapp.ui.community.opinions.detail.OpinionDetailViewModel
import com.tfm.musiccommunityapp.ui.community.recommendations.RecommendationsViewModel
import com.tfm.musiccommunityapp.ui.community.recommendations.detail.RecommendationDetailViewModel
import com.tfm.musiccommunityapp.ui.community.users.UsersViewModel
import com.tfm.musiccommunityapp.ui.home.HomeScreenViewModel
import com.tfm.musiccommunityapp.ui.login.LoginViewModel
import com.tfm.musiccommunityapp.ui.profile.ProfileViewModel
import com.tfm.musiccommunityapp.ui.scores.ScoresViewModel
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
            getCurrentUser = get(),
            isUserFollower = get(),
            followUser = get(),
            unfollowUser = get(),
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
            getCurrentUser = get(),
            updateEvent = get(),
            deleteEvent = get(),
            getCities = get(),
            createRecommendation = get(),
            getPostComments = get(),
            postOrRespondComment = get(),
            deleteComment = get(),
            dispatcher = get()
        )
    }

    viewModel {
        AdvertisementDetailViewModel(
            getAdvertisementById = get(),
            getPostImageByPostId = get(),
            getCurrentUser = get(),
            updateAdvertisement = get(),
            deleteAdvertisement = get(),
            getCities = get(),
            createRecommendation = get(),
            getPostComments = get(),
            postOrRespondComment = get(),
            deleteComment = get(),
            dispatcher = get()
        )
    }

    viewModel {
        DiscussionDetailViewModel(
            getDiscussionById = get(),
            getPostImageByPostId = get(),
            getCurrentUser = get(),
            updateDiscussion = get(),
            deleteDiscussion = get(),
            createRecommendation = get(),
            getPostComments = get(),
            postOrRespondComment = get(),
            deleteComment = get(),
            dispatcher = get()
        )
    }

    viewModel {
        OpinionDetailViewModel(
            getOpinionById = get(),
            getCurrentUser = get(),
            updateOpinion = get(),
            deleteOpinion = get(),
            createRecommendation = get(),
            getPostComments = get(),
            postOrRespondComment = get(),
            deleteComment = get(),
            dispatcher = get()
        )
    }

    viewModel {
        CommunityViewModel(
            getCities = get(),
            createEvent = get(),
            createAdvertisement = get(),
            createDiscussion = get(),
            dispatcher = get()
        )
    }

    viewModel {
        RecommendationsViewModel(
            getRecommendations = get(),
            dispatcher = get()
        )
    }

    viewModel {
        RecommendationDetailViewModel(
            getRecommendationById = get(),
            getPostImageByPostId = get(),
            getCurrentUser = get(),
            updateRecommendation = get(),
            deleteRecommendation = get(),
            rateRecommendation = get(),
            dispatcher = get()
        )
    }

    viewModel {
        ScoresViewModel(
            getCurrentUser = get(),
            getPrivateUserScores = get(),
            uploadScore = get(),
            dispatcher = get()
        )
    }

}