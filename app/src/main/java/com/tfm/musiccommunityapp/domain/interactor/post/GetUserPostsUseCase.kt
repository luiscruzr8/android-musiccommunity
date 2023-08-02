package com.tfm.musiccommunityapp.domain.interactor.post

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.CommonPostRepository

interface GetUserPostsUseCase {
    suspend operator fun invoke(login: String, type: String?, keyword: String?): GetUserPostsResult
}

class GetUserPostsUseCaseImpl(
    private val commonPostRepository: CommonPostRepository
) : GetUserPostsUseCase {
    override suspend operator fun invoke(login: String, type: String?, keyword: String?) : GetUserPostsResult =
        commonPostRepository.getUserPosts(login, type, keyword).fold(
            { it.toErrorResult() },
            { GetUserPostsResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetUserPostsResult.NetworkError(this)
    else -> GetUserPostsResult.GenericError(this)
}

sealed interface GetUserPostsResult {
    data class Success(val posts: List<GenericPostDomain>) : GetUserPostsResult
    data class NetworkError(val error: DomainError) : GetUserPostsResult
    data class GenericError(val error: DomainError) : GetUserPostsResult
}