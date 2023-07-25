package com.tfm.musiccommunityapp.data.model

import com.tfm.musiccommunityapp.data.api.model.FollowerResponse
import com.tfm.musiccommunityapp.data.api.model.UserResponse

object FollowerResponseBuilder {
    internal val default = FollowerResponse(
        id = 1,
        login = "User 1",
        bio= "Bio user 1",
        interests = TagResponseBuilder.defaultList()
    )
}