package com.tfm.musiccommunityapp.ui.community.events.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesResult
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.DeleteEventResult
import com.tfm.musiccommunityapp.domain.interactor.event.DeleteEventUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventByIdResult
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.UpdateEventResult
import com.tfm.musiccommunityapp.domain.interactor.event.UpdateEventUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageResult
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val getEventById: GetEventByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateEvent: UpdateEventUseCase,
    private val deleteEvent: DeleteEventUseCase,
    private val getCities: GetCitiesUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class EventOperationSuccess { UPDATE, DELETE }

    private val _event: MutableLiveData<EventDomain?> = MutableLiveData()
    private val _cities: MutableLiveData<List<CityDomain>> = MutableLiveData()
    private val _eventImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getEventByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showEventLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<EventOperationSuccess> =
        SingleLiveEvent()

    fun getEventLiveData() = _event as LiveData<EventDomain?>
    fun getCitiesLiveData() = _cities as LiveData<List<CityDomain>>
    fun getPostImageLiveData() = _eventImageURL as LiveData<String>
    fun getEventByIdError() = _getEventByIdError as LiveData<String>
    fun isEventLoading() = _showEventLoader as LiveData<Boolean>
    fun isUserOwnerLiveData() = _isOwnerUser as LiveData<Boolean>
    fun isOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<EventOperationSuccess>

    fun setUpEvent(eventId: Long) {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleGetEventByIdResult(getEventById(eventId))
            handleGetPostImageResult(getPostImageByPostId(eventId))
            handleGetCurrentUserResult(getCurrentUser())
            handleGetCitiesResult(getCities(null))
        }
    }

    private fun handleGetPostImageResult(result: GetPostImageResult) {
        when (result) {
            is GetPostImageResult.Success -> _eventImageURL.postValue(result.imageUrl)
        }
    }

    private fun handleGetEventByIdResult(result: GetEventByIdResult) {
        when (result) {
            GetEventByIdResult.NoEvent -> {
                _getEventByIdError.postValue("Error code: 404 - Event not found")
            }

            is GetEventByIdResult.Success -> {
                _event.postValue(result.event)
            }
        }
        _showEventLoader.postValue(false)
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isOwnerUser.postValue(result.user?.let { _event.value?.login == it.login }
                    ?: false)
            }

            GetCurrentUserResult.NoUser -> _isOwnerUser.postValue(false)
        }
    }

    private fun handleGetCitiesResult(result: GetCitiesResult) {
        when (result) {
            is GetCitiesResult.Success -> {
                _cities.postValue(result.cities)
            }
            else -> {
                _cities.postValue(emptyList())
            }
        }
    }

    fun sendDeleteEvent() {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleDeleteEventResult(deleteEvent(_event.value?.id ?: 0))
        }
    }

    private fun handleDeleteEventResult(result: DeleteEventResult) {
        when (result) {
            is DeleteEventResult.Success ->
                _isOperationSuccessful.postValue(EventOperationSuccess.DELETE)

            is DeleteEventResult.GenericError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is DeleteEventResult.NetworkError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendUpdateEvent(event: EventDomain) {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleUpdateEventResult(updateEvent(event))
        }
    }

    private fun handleUpdateEventResult(result: UpdateEventResult) {
        when (result) {
            is UpdateEventResult.Success ->
                _isOperationSuccessful.postValue(EventOperationSuccess.UPDATE)

            is UpdateEventResult.GenericError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is UpdateEventResult.NetworkError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

}