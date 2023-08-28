package com.tfm.musiccommunityapp.domain.di

import com.tfm.musiccommunityapp.domain.interactor.advertisement.CreateAdvertisementUseCase
import com.tfm.musiccommunityapp.domain.interactor.advertisement.CreateAdvertisementUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.advertisement.DeleteAdvertisementUseCase
import com.tfm.musiccommunityapp.domain.interactor.advertisement.DeleteAdvertisementUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementByIdUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementsUseCase
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementsUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.advertisement.UpdateAdvertisementUseCase
import com.tfm.musiccommunityapp.domain.interactor.advertisement.UpdateAdvertisementUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.city.GetClosestCitiesUseCase
import com.tfm.musiccommunityapp.domain.interactor.city.GetClosestCitiesUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.comment.DeleteCommentUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.DeleteCommentUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.comment.GetPostCommentsUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.GetPostCommentsUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.comment.PostOrRespondCommentUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.PostOrRespondCommentUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.discussion.CreateDiscussionUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.CreateDiscussionUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.discussion.DeleteDiscussionUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.DeleteDiscussionUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionByIdUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionsUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionsUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.discussion.UpdateDiscussionUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.UpdateDiscussionUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.event.CreateEventUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.CreateEventUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.event.DeleteEventUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.DeleteEventUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventByIdUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventsUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventsUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.event.UpdateEventUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.UpdateEventUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.opinion.CreateOpinionUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.CreateOpinionUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.opinion.DeleteOpinionUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.DeleteOpinionUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.opinion.GetOpinionByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.GetOpinionByIdUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.opinion.GetOpinionsUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.GetOpinionsUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.opinion.UpdateOpinionUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.UpdateOpinionUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostsByCityUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostsByCityUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostsByCoordinatesUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostsByCoordinatesUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.post.GetUserPostsUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetUserPostsUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.post.UploadPostImageUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.UploadPostImageUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.search.GetPostsByTagUseCase
import com.tfm.musiccommunityapp.domain.interactor.search.GetPostsByTagUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.search.GetUsersByTagUseCase
import com.tfm.musiccommunityapp.domain.interactor.search.GetUsersByTagUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.tag.GetTagsUseCase
import com.tfm.musiccommunityapp.domain.interactor.tag.GetTagsUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.FollowUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.FollowUserUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUsersUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUsersUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.IsUserFollowerOfUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.IsUserFollowerOfUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UnfollowUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UnfollowUserUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UpdateUserProfileUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UpdateUserProfileUseCaseImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    //region Login

    single {
        SignInUseCaseImpl(
            authRepository = get()
        )
    } bind SignInUseCase::class

    single {
        SignUpUseCaseImpl(
            authRepository = get()
        )
    } bind SignUpUseCase::class

    single {
        SignOutUseCaseImpl(
            authRepository = get()
        )
    } bind SignOutUseCase::class

    single {
        GetCurrentUserUseCaseImpl(
            authRepository = get(),
            userProfileRepository = get()
        )
    } bind GetCurrentUserUseCase::class

    //endregion

    //region User profile

    single {
        GetUserProfileUseCaseImpl(
            userProfileRepository = get()
        )
    } bind GetUserProfileUseCase::class

    single {
        GetUsersUseCaseImpl(
            userProfileRepository = get()
        )
    } bind GetUsersUseCase::class

    single {
        UpdateUserProfileUseCaseImpl(
            userProfileRepository = get()
        )
    } bind UpdateUserProfileUseCase::class

    //endregion

    //region Followers

    single {
        GetFollowersUseCaseImpl(
            userProfileRepository = get()
        )
    } bind GetFollowersUseCase::class

    single {
        GetFollowingUseCaseImpl(
            userProfileRepository = get()
        )
    } bind GetFollowingUseCase::class

    single {
        FollowUserUseCaseImpl(
            userProfileRepository = get()
        )
    } bind FollowUserUseCase::class

    single {
        UnfollowUserUseCaseImpl(
            userProfileRepository = get()
        )
    } bind UnfollowUserUseCase::class

    single {
        IsUserFollowerOfUseCaseImpl(
            userProfileRepository = get()
        )
    } bind IsUserFollowerOfUseCase::class

    //endregion

    //region Tags

    single {
        GetTagsUseCaseImpl(
            tagRepository = get()
        )
    } bind GetTagsUseCase::class

    //endregion

    //region SearchByTag

    single {
        GetPostsByTagUseCaseImpl(
            tagRepository = get()
        )
    } bind GetPostsByTagUseCase::class

    single {
        GetUsersByTagUseCaseImpl(
            tagRepository = get()
        )
    } bind GetUsersByTagUseCase::class

    //endregion

    //region Generic posts

    single {
        GetUserPostsUseCaseImpl(
            commonPostRepository = get()
        )
    } bind GetUserPostsUseCase::class

    single {
        GetPostsByCityUseCaseImpl(
            commonPostRepository = get()
        )
    } bind GetPostsByCityUseCase::class

    single {
        GetPostsByCoordinatesUseCaseImpl(
            commonPostRepository = get()
        )
    } bind GetPostsByCoordinatesUseCase::class

    single {
        GetPostImageUseCaseImpl(
            commonPostRepository = get()
        )
    } bind GetPostImageUseCase::class

    single {
        UploadPostImageUseCaseImpl(
            commonPostRepository = get()
        )
    } bind UploadPostImageUseCase::class

    //endregion

    //region Comments

    single {
        GetPostCommentsUseCaseImpl(
            commentRepository = get()
        )
    } bind GetPostCommentsUseCase::class

    single {
        DeleteCommentUseCaseImpl(
            commentRepository = get()
        )
    } bind DeleteCommentUseCase::class

    single {
        PostOrRespondCommentUseCaseImpl(
            commentRepository = get()
        )
    } bind PostOrRespondCommentUseCase::class

    //endregion

    //region Advertisements

    single {
        GetAdvertisementsUseCaseImpl(
            advertisementRepository = get()
        )
    } bind GetAdvertisementsUseCase::class

    single {
        GetAdvertisementByIdUseCaseImpl(
            advertisementRepository = get()
        )
    } bind GetAdvertisementByIdUseCase::class

    single {
        CreateAdvertisementUseCaseImpl(
            advertisementRepository = get()
        )
    } bind CreateAdvertisementUseCase::class

    single {
        UpdateAdvertisementUseCaseImpl(
            advertisementRepository = get()
        )
    } bind UpdateAdvertisementUseCase::class

    single {
        DeleteAdvertisementUseCaseImpl(
            advertisementRepository = get()
        )
    } bind DeleteAdvertisementUseCase::class

    //endregion

    //region Events

    single {
        GetEventsUseCaseImpl(
            eventRepository = get()
        )
    } bind GetEventsUseCase::class

    single {
        GetEventByIdUseCaseImpl(
            eventRepository = get()
        )
    } bind GetEventByIdUseCase::class

    single {
        CreateEventUseCaseImpl(
            eventRepository = get()
        )
    } bind CreateEventUseCase::class

    single {
        UpdateEventUseCaseImpl(
            eventRepository = get()
        )
    } bind UpdateEventUseCase::class

    single {
        DeleteEventUseCaseImpl(
            eventRepository = get()
        )
    } bind DeleteEventUseCase::class

    //endregion

    //region Discussions

    single {
        GetDiscussionsUseCaseImpl(
            discussionRepository = get()
        )
    } bind GetDiscussionsUseCase::class

    single {
        GetDiscussionByIdUseCaseImpl(
            discussionRepository = get()
        )
    } bind GetDiscussionByIdUseCase::class

    single {
        CreateDiscussionUseCaseImpl(
            discussionRepository = get()
        )
    } bind CreateDiscussionUseCase::class

    single {
        UpdateDiscussionUseCaseImpl(
            discussionRepository = get()
        )
    } bind UpdateDiscussionUseCase::class

    single {
        DeleteDiscussionUseCaseImpl(
            discussionRepository = get()
        )
    } bind DeleteDiscussionUseCase::class

    //endregion

    //region Opinions

    single {
        GetOpinionsUseCaseImpl(
            opinionRepository = get()
        )
    } bind GetOpinionsUseCase::class

    single {
        GetOpinionByIdUseCaseImpl(
            opinionRepository = get()
        )
    } bind GetOpinionByIdUseCase::class

    single {
        CreateOpinionUseCaseImpl(
            opinionRepository = get()
        )
    } bind CreateOpinionUseCase::class

    single {
        UpdateOpinionUseCaseImpl(
            opinionRepository = get()
        )
    } bind UpdateOpinionUseCase::class

    single {
        DeleteOpinionUseCaseImpl(
            opinionRepository = get()
        )
    } bind DeleteOpinionUseCase::class

    //endregion

    //region Cities

    single {
        GetCitiesUseCaseImpl(
            cityRepository = get()
        )
    } bind GetCitiesUseCase::class

    single {
        GetClosestCitiesUseCaseImpl(
            cityRepository = get()
        )
    } bind GetClosestCitiesUseCase::class

    //endregion

}