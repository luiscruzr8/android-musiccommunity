package com.tfm.musiccommunityapp.domain.interactor.post

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.CommonPostRepository

interface GetPostsByCoordinatesUseCase {
    suspend operator fun invoke(latitude: Double, longitude: Double, checkClosest: Boolean): GetPostsByCoordinatesResult
}

class GetPostsByCoordinatesUseCaseImpl(
    private val commonPostRepository: CommonPostRepository
) : GetPostsByCoordinatesUseCase {

    override suspend fun invoke(latitude: Double, longitude: Double, checkClosest: Boolean): GetPostsByCoordinatesResult =
        commonPostRepository.getPostsByCoordinates(latitude, longitude, checkClosest).fold(
            { it.toErrorResult() },
            { GetPostsByCoordinatesResult.Success(it) }
        )

}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetPostsByCoordinatesResult.NetworkError(this)
    else -> GetPostsByCoordinatesResult.GenericError(this)
}

sealed interface GetPostsByCoordinatesResult {
    data class Success(val posts: List<GenericPostDomain>) : GetPostsByCoordinatesResult
    data class NetworkError(val error: DomainError) : GetPostsByCoordinatesResult
    data class GenericError(val error: DomainError) : GetPostsByCoordinatesResult
}