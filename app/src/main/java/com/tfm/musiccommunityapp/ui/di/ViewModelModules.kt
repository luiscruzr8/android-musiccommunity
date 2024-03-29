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
import com.tfm.musiccommunityapp.ui.scores.detail.ScoreDetailViewModel
import com.tfm.musiccommunityapp.ui.search.city.SearchByCityViewModel
import com.tfm.musiccommunityapp.ui.search.tag.SearchByTagViewModel
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
            getUserPosts = get(),
            getOnlyUserRecommendations = get(),
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
            uploadImagePost = get(),
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
            uploadImagePost = get(),
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
            uploadImagePost = get(),
            dispatcher = get()
        )
    }

    viewModel {
        OpinionDetailViewModel(
            getCurrentUser = get(),
            getOpinionById = get(),
            getScoreFileById = get(),
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
            getMyScores = get(),
            createEvent = get(),
            createAdvertisement = get(),
            createDiscussion = get(),
            createOpinion = get(),
            uploadImagePost = get(),
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
            getPublicUserScores = get(),
            uploadScore = get(),
            dispatcher = get()
        )
    }

    viewModel {
        ScoreDetailViewModel(
            getCurrentUser = get(),
            getScoreInfoById = get(),
            getScoreFileById = get(),
            deleteScore = get(),
            createOpinion = get(),
            dispatcher = get()
        )
    }

    viewModel {
        SearchByCityViewModel(
            getCities = get(),
            searchPostsByCityName = get(),
            searchPostsByCoordinates = get(),
            dispatcher = get()
        )
    }

    viewModel {
        SearchByTagViewModel(
            getTags = get(),
            searchPostsByTag = get(),
            searchUsersByInterests = get(),
            dispatcher = get()
        )
    }

}