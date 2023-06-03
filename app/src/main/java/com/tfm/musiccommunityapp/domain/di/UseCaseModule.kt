package com.tfm.musiccommunityapp.domain.di

import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCaseImpl
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCaseImpl
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

    //endregion

    //region X



    //endregion

    //region Y



    //endregion

    //region Z



    //endregion

}