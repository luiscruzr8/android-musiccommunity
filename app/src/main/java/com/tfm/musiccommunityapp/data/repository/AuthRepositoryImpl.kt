package com.tfm.musiccommunityapp.data.repository

import android.util.Log
import com.tfm.musiccommunityapp.api.model.SignInResponse
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import com.tfm.musiccommunityapp.data.datasource.LoginDatasource
import com.tfm.musiccommunityapp.domain.model.AuthError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.model.ServerError
import com.tfm.musiccommunityapp.domain.model.ValidationError
import com.tfm.musiccommunityapp.usecase.repository.AuthRepository
import com.tfm.musiccommunityapp.usecase.repository.SignInStatus
import com.tfm.musiccommunityapp.usecase.repository.SignOutStatus
import com.tfm.musiccommunityapp.usecase.repository.SignUpStatus
import okhttp3.Cache

class AuthRepositoryImpl(
    private val authDatasource: AuthDatasource,
    private val loginRemoteDatasource: LoginDatasource,
    private val cache: Cache,
): AuthRepository {
    override fun isSignedIn(): Boolean = authDatasource.accessToken.isNotEmpty()

    override suspend fun refreshLogin(): SignInStatus {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(username: String, password: String): SignInStatus {
        try {
            loginRemoteDatasource.signIn(username, password).let { result ->
                return result.fold(
                    ifLeft = {
                        when (it) {
                            is AuthError, is NotFoundError, is ValidationError -> SignInStatus.AUTH_ERROR
                            is NetworkError -> SignInStatus.NETWORK_ERROR
                            is ServerError -> SignInStatus.SERVER_ERROR
                            else -> SignInStatus.UNKNOWN_ERROR
                        }
                    },
                    ifRight = {
                        processSuccess(username, password, it)
                    }
                )
            }
        } catch (Exception: Exception) {
            Log.e("AuthRepositoryImpl", "Error signing in: ${Exception.message}")
            return SignInStatus.UNKNOWN_ERROR
        }
    }

    override suspend fun signUp(username: String, password: String, email: String, phoneNumber: String): SignUpStatus {
        try {
            loginRemoteDatasource.signUp(username, password, email, phoneNumber).let { result ->
                return result.fold(
                    ifLeft = {
                        when (it) {
                            is AuthError, is NotFoundError -> SignUpStatus.AUTH_ERROR
                            is ValidationError -> SignUpStatus.BAD_REQUEST
                            is NetworkError -> SignUpStatus.NETWORK_ERROR
                            is ServerError -> SignUpStatus.SERVER_ERROR
                            else -> SignUpStatus.UNKNOWN_ERROR
                        }
                    },
                    ifRight = {
                        SignUpStatus.SUCCESS
                    }
                )
            }
        } catch (Exception: Exception) {
            Log.e("AuthRepositoryImpl", "Error signing up: ${Exception.message}")
            return SignUpStatus.UNKNOWN_ERROR
        }
    }

    override suspend fun logout(): SignOutStatus {
        logoutAuth()
        return SignOutStatus.SUCCESS
    }

    private fun processSuccess(username: String, password: String, response: SignInResponse): SignInStatus {
        authDatasource.storeCredentials(username, password)
        authDatasource.storeToken(response.accessToken)
        return SignInStatus.SUCCESS
    }

    private fun logoutAuth() {
        authDatasource.clearData()
        cache.directory.deleteRecursively()
    }
}