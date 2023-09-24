package com.tfm.musiccommunityapp.api.model

import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import java.time.LocalDateTime

internal data class RecommendationResponse(
    val id: Long,
    val creationDateTime: LocalDateTime,
    val recTitle: String,
    val recText: String,
    val login: String,
    val post: GenericPostResponse,
    val postId: Long,
    val rating: Double
)

internal fun RecommendationResponse.toDomain() = RecommendationDomain(
    id = id,
    createdOn = creationDateTime,
    recommendationTitle = recTitle,
    recommendationMessage = recText,
    login = login,
    post = post.toDomain(),
    postId = postId,
    rating = rating
)

internal fun RecommendationDomain.toResponse() = RecommendationResponse(
    id = id,
    creationDateTime = createdOn,
    recTitle = recommendationTitle,
    recText = recommendationMessage,
    login = login,
    post = post.toResponse(),
    postId = postId,
    rating = rating
)