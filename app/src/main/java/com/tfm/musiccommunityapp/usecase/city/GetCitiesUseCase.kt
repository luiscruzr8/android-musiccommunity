package com.tfm.musiccommunityapp.usecase.city

import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.CityRepository

interface GetCitiesUseCase {
    suspend operator fun invoke(keyword: String?) : GetCitiesResult
}

internal class GetCitiesUseCaseImpl(
    private val cityRepository: CityRepository
) : GetCitiesUseCase {
    override suspend fun invoke(keyword: String?) : GetCitiesResult =
        cityRepository.getCities(keyword).fold(
            { it.toErrorResult() },
            { GetCitiesResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetCitiesResult.NetworkError(this)
    else -> GetCitiesResult.GenericError(this)
}

sealed interface GetCitiesResult {
    data class Success(val cities: List<CityDomain>) : GetCitiesResult
    data class NetworkError(val error: DomainError) : GetCitiesResult
    data class GenericError(val error: DomainError) : GetCitiesResult
}