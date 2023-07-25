package com.tfm.musiccommunityapp.data.datasource

import arrow.core.Either
import com.tfm.musiccommunityapp.data.api.model.SignInResponse
import com.tfm.musiccommunityapp.data.api.model.SignUpResponse
import com.tfm.musiccommunityapp.domain.model.DomainError
import retrofit2.Response

interface LoginDatasource {

    suspend fun signIn(username: String, password: String): Either<DomainError, SignInResponse>

    suspend fun signUp(username: String, password: String, email: String, phone: String): Either<DomainError, SignUpResponse>

}