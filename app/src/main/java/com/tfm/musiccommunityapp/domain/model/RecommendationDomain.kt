package com.tfm.musiccommunityapp.domain.model

import java.time.LocalDateTime

data class RecommendationDomain(
    val id: Long,
    val createdOn: LocalDateTime,
    val recommendationTitle: String,
    val recommendationMessage: String,
    val login: String,
    val post: GenericPostDomain,
    val postId: Long,
    val rating: Double
)