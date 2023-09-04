package com.tfm.musiccommunityapp.usecase.di

import com.tfm.musiccommunityapp.usecase.advertisement.CreateAdvertisementUseCase
import com.tfm.musiccommunityapp.usecase.advertisement.CreateAdvertisementUseCaseImpl
import com.tfm.musiccommunityapp.usecase.advertisement.DeleteAdvertisementUseCase
import com.tfm.musiccommunityapp.usecase.advertisement.DeleteAdvertisementUseCaseImpl
import com.tfm.musiccommunityapp.usecase.advertisement.GetAdvertisementByIdUseCase
import com.tfm.musiccommunityapp.usecase.advertisement.GetAdvertisementByIdUseCaseImpl
import com.tfm.musiccommunityapp.usecase.advertisement.GetAdvertisementsUseCase
import com.tfm.musiccommunityapp.usecase.advertisement.GetAdvertisementsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.advertisement.UpdateAdvertisementUseCase
import com.tfm.musiccommunityapp.usecase.advertisement.UpdateAdvertisementUseCaseImpl
import com.tfm.musiccommunityapp.usecase.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.usecase.city.GetCitiesUseCaseImpl
import com.tfm.musiccommunityapp.usecase.city.GetClosestCitiesUseCase
import com.tfm.musiccommunityapp.usecase.city.GetClosestCitiesUseCaseImpl
import com.tfm.musiccommunityapp.usecase.comment.DeleteCommentUseCase
import com.tfm.musiccommunityapp.usecase.comment.DeleteCommentUseCaseImpl
import com.tfm.musiccommunityapp.usecase.comment.GetPostCommentsUseCase
import com.tfm.musiccommunityapp.usecase.comment.GetPostCommentsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.comment.PostOrRespondCommentUseCase
import com.tfm.musiccommunityapp.usecase.comment.PostOrRespondCommentUseCaseImpl
import com.tfm.musiccommunityapp.usecase.discussion.CreateDiscussionUseCase
import com.tfm.musiccommunityapp.usecase.discussion.CreateDiscussionUseCaseImpl
import com.tfm.musiccommunityapp.usecase.discussion.DeleteDiscussionUseCase
import com.tfm.musiccommunityapp.usecase.discussion.DeleteDiscussionUseCaseImpl
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionByIdUseCase
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionByIdUseCaseImpl
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionsUseCase
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.discussion.UpdateDiscussionUseCase
import com.tfm.musiccommunityapp.usecase.discussion.UpdateDiscussionUseCaseImpl
import com.tfm.musiccommunityapp.usecase.event.CreateEventUseCase
import com.tfm.musiccommunityapp.usecase.event.CreateEventUseCaseImpl
import com.tfm.musiccommunityapp.usecase.event.DeleteEventUseCase
import com.tfm.musiccommunityapp.usecase.event.DeleteEventUseCaseImpl
import com.tfm.musiccommunityapp.usecase.event.GetEventByIdUseCase
import com.tfm.musiccommunityapp.usecase.event.GetEventByIdUseCaseImpl
import com.tfm.musiccommunityapp.usecase.event.GetEventsUseCase
import com.tfm.musiccommunityapp.usecase.event.GetEventsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.event.UpdateEventUseCase
import com.tfm.musiccommunityapp.usecase.event.UpdateEventUseCaseImpl
import com.tfm.musiccommunityapp.usecase.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.usecase.login.GetCurrentUserUseCaseImpl
import com.tfm.musiccommunityapp.usecase.login.SignInUseCase
import com.tfm.musiccommunityapp.usecase.login.SignInUseCaseImpl
import com.tfm.musiccommunityapp.usecase.login.SignOutUseCase
import com.tfm.musiccommunityapp.usecase.login.SignOutUseCaseImpl
import com.tfm.musiccommunityapp.usecase.login.SignUpUseCase
import com.tfm.musiccommunityapp.usecase.login.SignUpUseCaseImpl
import com.tfm.musiccommunityapp.usecase.opinion.CreateOpinionUseCase
import com.tfm.musiccommunityapp.usecase.opinion.CreateOpinionUseCaseImpl
import com.tfm.musiccommunityapp.usecase.opinion.DeleteOpinionUseCase
import com.tfm.musiccommunityapp.usecase.opinion.DeleteOpinionUseCaseImpl
import com.tfm.musiccommunityapp.usecase.opinion.GetOpinionByIdUseCase
import com.tfm.musiccommunityapp.usecase.opinion.GetOpinionByIdUseCaseImpl
import com.tfm.musiccommunityapp.usecase.opinion.GetOpinionsUseCase
import com.tfm.musiccommunityapp.usecase.opinion.GetOpinionsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.opinion.UpdateOpinionUseCase
import com.tfm.musiccommunityapp.usecase.opinion.UpdateOpinionUseCaseImpl
import com.tfm.musiccommunityapp.usecase.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.usecase.post.GetPostImageUseCaseImpl
import com.tfm.musiccommunityapp.usecase.post.GetPostsByCityUseCase
import com.tfm.musiccommunityapp.usecase.post.GetPostsByCityUseCaseImpl
import com.tfm.musiccommunityapp.usecase.post.GetPostsByCoordinatesUseCase
import com.tfm.musiccommunityapp.usecase.post.GetPostsByCoordinatesUseCaseImpl
import com.tfm.musiccommunityapp.usecase.post.GetUserPostsUseCase
import com.tfm.musiccommunityapp.usecase.post.GetUserPostsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.post.UploadPostImageUseCase
import com.tfm.musiccommunityapp.usecase.post.UploadPostImageUseCaseImpl
import com.tfm.musiccommunityapp.usecase.recommendations.CreateRecommendationUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.CreateRecommendationUseCaseImpl
import com.tfm.musiccommunityapp.usecase.recommendations.DeleteRecommendationUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.DeleteRecommendationUseCaseImpl
import com.tfm.musiccommunityapp.usecase.recommendations.GetRecommendationByIdUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.GetRecommendationByIdUseCaseImpl
import com.tfm.musiccommunityapp.usecase.recommendations.GetRecommendationsUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.GetRecommendationsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.recommendations.GetUserRecommendationsUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.GetUserRecommendationsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.recommendations.RateRecommendationUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.RateRecommendationUseCaseImpl
import com.tfm.musiccommunityapp.usecase.recommendations.UpdateRecommendationUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.UpdateRecommendationUseCaseImpl
import com.tfm.musiccommunityapp.usecase.score.DeleteScoreUseCase
import com.tfm.musiccommunityapp.usecase.score.DeleteScoreUseCaseImpl
import com.tfm.musiccommunityapp.usecase.score.GetScoreFileUseCase
import com.tfm.musiccommunityapp.usecase.score.GetScoreFileUseCaseImpl
import com.tfm.musiccommunityapp.usecase.score.GetScoreInfoByIdUseCase
import com.tfm.musiccommunityapp.usecase.score.GetScoreInfoByIdUseCaseImpl
import com.tfm.musiccommunityapp.usecase.score.GetUserScoresUseCase
import com.tfm.musiccommunityapp.usecase.score.GetUserScoresUseCaseImpl
import com.tfm.musiccommunityapp.usecase.score.UploadScoreUseCase
import com.tfm.musiccommunityapp.usecase.score.UploadScoreUseCaseImpl
import com.tfm.musiccommunityapp.usecase.search.GetPostsByTagUseCase
import com.tfm.musiccommunityapp.usecase.search.GetPostsByTagUseCaseImpl
import com.tfm.musiccommunityapp.usecase.search.GetUsersByTagUseCase
import com.tfm.musiccommunityapp.usecase.search.GetUsersByTagUseCaseImpl
import com.tfm.musiccommunityapp.usecase.tag.GetTagsUseCase
import com.tfm.musiccommunityapp.usecase.tag.GetTagsUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.FollowUserUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.FollowUserUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.GetFollowersUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.GetFollowersUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.GetFollowingUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.GetFollowingUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.GetUserProfileUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.GetUserProfileUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.GetUsersUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.GetUsersUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.IsUserFollowerOfUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.IsUserFollowerOfUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.UnfollowUserUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.UnfollowUserUseCaseImpl
import com.tfm.musiccommunityapp.usecase.userprofile.UpdateUserProfileUseCase
import com.tfm.musiccommunityapp.usecase.userprofile.UpdateUserProfileUseCaseImpl
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

    //region Recommendations

    single {
        CreateRecommendationUseCaseImpl(
            recommendationsRepository = get()
        )
    } bind CreateRecommendationUseCase::class

    single {
        DeleteRecommendationUseCaseImpl(
            recommendationsRepository = get()
        )
    } bind DeleteRecommendationUseCase::class

    single {
        GetRecommendationByIdUseCaseImpl(
            recommendationsRepository = get()
        )
    } bind GetRecommendationByIdUseCase::class

    single {
        GetRecommendationsUseCaseImpl(
            recommendationsRepository = get()
        )
    } bind GetRecommendationsUseCase::class

    single {
        GetUserRecommendationsUseCaseImpl(
                recommendationsRepository = get()
        )
    } bind GetUserRecommendationsUseCase::class

    single {
        RateRecommendationUseCaseImpl(
            recommendationsRepository = get()
        )
    } bind RateRecommendationUseCase::class

    single {
        UpdateRecommendationUseCaseImpl(
            recommendationsRepository = get()
        )
    } bind UpdateRecommendationUseCase::class

    //endregion

    //region Scores

    single {
        GetUserScoresUseCaseImpl(
            scoreRepository = get()
        )
    } bind GetUserScoresUseCase::class

    single {
        GetScoreInfoByIdUseCaseImpl(
            scoreRepository = get()
        )
    } bind GetScoreInfoByIdUseCase::class

    single {
        DeleteScoreUseCaseImpl(
            scoreRepository = get()
        )
    } bind DeleteScoreUseCase::class

    single {
        UploadScoreUseCaseImpl(
            scoreRepository = get()
        )
    } bind UploadScoreUseCase::class

    single {
        GetScoreFileUseCaseImpl(
            scoreRepository = get()
        )
    } bind GetScoreFileUseCase::class

    //endregion

}