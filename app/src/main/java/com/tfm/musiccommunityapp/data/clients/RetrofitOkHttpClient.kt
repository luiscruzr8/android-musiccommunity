package com.tfm.musiccommunityapp.data.clients

import com.google.gson.Gson
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitOkHttpClient(
    private val baseUrl: String,
    private val baseOkHttpClient: OkHttpClient,
    private val gson: Gson
) {
    fun build(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(baseOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}