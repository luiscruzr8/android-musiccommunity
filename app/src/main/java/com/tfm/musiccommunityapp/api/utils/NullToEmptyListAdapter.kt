package com.tfm.musiccommunityapp.api.utils

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.FromJson

@Retention(AnnotationRetention.RUNTIME)
@JsonAdapter(NullToEmptyListAdapter::class)
annotation class NullToEmptyList

class NullToEmptyListAdapter {

    @FromJson
    @NullToEmptyList
    fun fromJson(data: List<String>?): List<String> {
        return data ?: emptyList()
    }
}