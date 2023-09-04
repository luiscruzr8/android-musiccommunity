package com.tfm.musiccommunityapp.api.model

import com.tfm.musiccommunityapp.domain.model.TagDomain

internal data class TagResponse(
    val tagName: String
)

internal fun TagResponse.toDomain() = TagDomain(
    tagName = tagName
)

internal fun TagDomain.toResponse() = TagResponse(
    tagName = tagName
)