package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig

interface ScoresApi {

    companion object {
        const val API_SCORES_URL = BuildConfig.BACKEND_URL + "/scores"
    }

}