package com.tfm.musiccommunityapp.usecase.city

import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.CityRepository

interface GetClosestCitiesUseCase {
    suspend operator fun invoke(latitude: Double, longitude: Double) : GetClosestCitiesResult
}

internal class GetClosestCitiesUseCaseImpl(
    private val cityRepository: CityRepository
) : GetClosestCitiesUseCase {
    override suspend fun invoke(latitude: Double, longitude: Double) : GetClosestCitiesResult =
        cityRepository.getClosestCitiesByCoordinates(latitude, longitude).fold(
            { it.toErrorResult() },
            { GetClosestCitiesResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetClosestCitiesResult.NetworkError(this)
    else -> GetClosestCitiesResult.GenericError(this)
}

sealed interface GetClosestCitiesResult {
    data class Success(val cities: List<CityDomain>) : GetClosestCitiesResult
    data class NetworkError(val error: DomainError) : GetClosestCitiesResult
    data class GenericError(val error: DomainError) : GetClosestCitiesResult
}