package com.tfm.musiccommunityapp.usecase.post

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.CommonPostRepository

interface GetPostsByCityUseCase {
    suspend operator fun invoke(cityName: String): GetPostByCityResult
}

class GetPostsByCityUseCaseImpl(
    private val commonPostRepository: CommonPostRepository
) : GetPostsByCityUseCase {
    override suspend operator fun invoke(cityName: String) : GetPostByCityResult =
        commonPostRepository.getPostsByCity(cityName).fold(
            { it.toErrorResult() },
            { GetPostByCityResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetPostByCityResult.NetworkError(this)
    else -> GetPostByCityResult.GenericError(this)
}

sealed interface GetPostByCityResult {
    data class Success(val posts: List<GenericPostDomain>) : GetPostByCityResult
    data class NetworkError(val error: DomainError) : GetPostByCityResult
    data class GenericError(val error: DomainError) : GetPostByCityResult
}