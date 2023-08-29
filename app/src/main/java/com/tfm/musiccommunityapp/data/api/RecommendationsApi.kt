package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig

interface RecommendationsApi {

    companion object {
        const val API_RECOMMENDATIONS_URL = BuildConfig.BACKEND_URL + "/recommendations"
    }
}