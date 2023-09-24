package com.tfm.musiccommunityapp.data.datasource

import arrow.core.Either
import com.tfm.musiccommunityapp.api.model.SignInResponse
import com.tfm.musiccommunityapp.api.model.SignUpResponse
import com.tfm.musiccommunityapp.domain.model.DomainError

interface LoginDatasource {

    suspend fun signIn(
        username: String,
        password: String,
        firebaseToken: String
    ): Either<DomainError, SignInResponse>

    suspend fun signUp(
        username: String,
        password: String,
        email: String,
        phone: String,
        firebaseToken: String
    ): Either<DomainError, SignUpResponse>

}