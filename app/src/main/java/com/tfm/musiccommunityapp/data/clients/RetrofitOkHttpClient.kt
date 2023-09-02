package com.tfm.musiccommunityapp.data.clients

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitOkHttpClient(
    private val baseUrl: String,
    private val baseOkHttpClient: OkHttpClient,
    private val gson: Gson
) {
    fun build(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(baseOkHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}