package com.tfm.musiccommunityapp.data.api.model

import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain

internal data class UserResponse(
    val id: Long,
    val login: String,
    val email: String,
    val phone: String,
    val bio: String?,
    val interests: List<TagResponse>
)

internal fun UserResponse.toDomain() = UserDomain(
    id = id,
    login = login,
    email = email,
    phone = phone,
    bio = bio ?: "",
    interests = interests.map { it.toDomain() }
)

internal fun UserDomain.toResponse() = UserResponse(
    id = id,
    login = login,
    email = email,
    phone = phone,
    bio = bio,
    interests = interests.map { it.toResponse() }
)

internal data class FollowerResponse(
    val id: Long,
    val login: String,
    val bio: String?,
    val interests: List<TagResponse>
)

internal fun FollowerResponse.toDomain() = ShortUserDomain(
    id = id,
    login = login,
    bio = bio ?: "",
    interests = interests.map { it.toDomain() }
)
