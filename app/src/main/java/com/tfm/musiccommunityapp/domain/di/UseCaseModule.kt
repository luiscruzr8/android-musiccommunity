package com.tfm.musiccommunityapp.domain.di

import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.search.GetPostsByTagUseCase
import com.tfm.musiccommunityapp.domain.interactor.search.GetPostsByTagUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.search.GetUsersByTagUseCase
import com.tfm.musiccommunityapp.domain.interactor.search.GetUsersByTagUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.tags.GetTagsUseCase
import com.tfm.musiccommunityapp.domain.interactor.tags.GetTagsUseCaseImpl
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

    //region Z


    //endregion

}