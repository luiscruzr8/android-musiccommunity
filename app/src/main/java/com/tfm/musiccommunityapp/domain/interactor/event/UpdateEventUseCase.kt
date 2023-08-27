package com.tfm.musiccommunityapp.domain.interactor.event

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.EventRepository

interface UpdateEventUseCase {
    suspend operator fun invoke(updateEvent: EventDomain) : UpdateEventResult
}

class UpdateEventUseCaseImpl(
    private val eventRepository: EventRepository
) : UpdateEventUseCase {
    override suspend operator fun invoke(updateEvent: EventDomain) : UpdateEventResult =
        eventRepository.updateEvent(updateEvent).fold(
            { it.toErrorResult() },
            { UpdateEventResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> UpdateEventResult.NetworkError(this)
    else -> UpdateEventResult.GenericError(this)
}

sealed interface UpdateEventResult {
    data class Success(val id: Long) : UpdateEventResult
    data class NetworkError(val error: DomainError) : UpdateEventResult
    data class GenericError(val error: DomainError) : UpdateEventResult
}