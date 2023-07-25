package com.tfm.musiccommunityapp.domain.interactor.model

import com.tfm.musiccommunityapp.domain.model.UserDomain

object UserDomainBuilder {
    val default = UserDomain(
        id = 1,
        login = "User 1",
        email = "user1@mail.com",
        phone = "123456789",
        bio= "Bio user 1",
        interests = TagDomainBuilder.defaultList()
    )
}