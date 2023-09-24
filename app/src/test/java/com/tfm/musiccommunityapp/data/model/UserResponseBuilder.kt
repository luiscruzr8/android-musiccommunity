package com.tfm.musiccommunityapp.data.model

import com.tfm.musiccommunityapp.api.model.UserResponse


object UserResponseBuilder {
    internal val default = UserResponse(
        id = 1,
        login = "User 1",
        email = "user1@mail.com",
        phone = "123456789",
        bio= "Bio user 1",
        interests = TagResponseBuilder.defaultList()
    )
}