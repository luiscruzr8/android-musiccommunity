package com.tfm.musiccommunityapp.domain.interactor.event

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.EventRepository

interface GetEventsUseCase {
    suspend operator fun invoke(keyword: String?) : GetEventsResult
}

class GetEventsUseCaseImpl(
    private val eventRepository: EventRepository
) : GetEventsUseCase {
    override suspend fun invoke(keyword: String?) : GetEventsResult =
        eventRepository.getEvents(keyword).fold(
            { it.toErrorResult() },
            { GetEventsResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetEventsResult.NetworkError(this)
    else -> GetEventsResult.GenericError(this)
}

sealed interface GetEventsResult {
    data class Success(val events: List<EventDomain>) : GetEventsResult
    data class NetworkError(val error: DomainError) : GetEventsResult
    data class GenericError(val error: DomainError) : GetEventsResult
}