package com.tfm.musiccommunityapp.data.api.model

import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import java.time.LocalDateTime

internal data class GenericPostResponse(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val type: String,
    val creationDateTime: LocalDateTime,
    val tags: List<TagResponse>
)

internal fun GenericPostResponse.toDomain() = GenericPostDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    type = type,
    creationDateTime = creationDateTime,
    tags = tags.map { it.toDomain() }
)