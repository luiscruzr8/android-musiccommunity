package com.tfm.musiccommunityapp.domain.model

data class UserDomain (
    val id: Long,
    val login: String,
    val email: String,
    val phone: String,
    val bio: String,
    val interests: List<TagDomain>
)

data class ShortUserDomain(
    val id: Long,
    val login: String,
    val bio: String,
    val interests: List<TagDomain>
)