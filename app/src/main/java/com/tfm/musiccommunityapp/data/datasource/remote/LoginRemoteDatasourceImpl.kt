package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.tfm.musiccommunityapp.api.AuthApi
import com.tfm.musiccommunityapp.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.api.model.SignInBody
import com.tfm.musiccommunityapp.api.model.SignInResponse
import com.tfm.musiccommunityapp.api.model.SignUpBody
import com.tfm.musiccommunityapp.api.model.SignUpResponse
import com.tfm.musiccommunityapp.api.model.toDomain
import com.tfm.musiccommunityapp.data.datasource.LoginDatasource
import com.tfm.musiccommunityapp.domain.model.DomainError
import retrofit2.HttpException

class LoginRemoteDatasourceImpl(private val authApi: AuthApi): LoginDatasource {

    @Throws(HttpException::class)
    override suspend fun signIn(
        username: String,
        password: String,
        firebaseToken: String
    ): Either<DomainError, SignInResponse> {
        val signInBody = SignInBody(username, password, firebaseToken)
        authApi.signIn(signInBody).let { result ->
            return when {
                !result.isSuccessful -> result.toErrorResponse().toDomain().left()
                else -> result.body().let { it as SignInResponse }.right()
            }
        }
    }

    override suspend fun signUp(
        username: String,
        password: String,
        email: String,
        phone: String,
        firebaseToken: String
    ): Either<DomainError, SignUpResponse> {
        val signUpBody = SignUpBody(username, email, password, phone, firebaseToken)
        authApi.signUp(signUpBody).let { result ->
            return when {
                !result.isSuccessful -> result.toErrorResponse().toDomain().left()
                else -> result.body().let { it as SignUpResponse }.right()
            }
        }
    }

}