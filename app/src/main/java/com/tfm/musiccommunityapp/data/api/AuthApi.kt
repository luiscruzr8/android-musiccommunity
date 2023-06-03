package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.model.SignInBody
import com.tfm.musiccommunityapp.data.api.model.SignInResponse
import com.tfm.musiccommunityapp.data.api.model.SignUpBody
import com.tfm.musiccommunityapp.data.api.model.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    companion object {
        const val API_AUTH_URL = BuildConfig.BACKEND_URL + "auth"
    }


    @POST("$API_AUTH_URL/signin")
    suspend fun signIn(@Body signInForm: SignInBody): Response<SignInResponse>

    @POST("$API_AUTH_URL/signup")
    suspend fun signUp(@Body signUpForm: SignUpBody): Response<SignUpResponse>

}