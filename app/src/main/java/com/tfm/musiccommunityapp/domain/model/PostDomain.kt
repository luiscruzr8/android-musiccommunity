package com.tfm.musiccommunityapp.domain.model

import java.time.LocalDateTime

data class GenericPostDomain(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val type: String,
    val creationDateTime: LocalDateTime,
    val tags: List<TagDomain>
)