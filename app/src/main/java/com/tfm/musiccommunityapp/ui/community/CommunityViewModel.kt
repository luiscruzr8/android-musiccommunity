package com.tfm.musiccommunityapp.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.advertisement.CreateAdvertisementResult
import com.tfm.musiccommunityapp.domain.interactor.advertisement.CreateAdvertisementUseCase
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesResult
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.CreateDiscussionResult
import com.tfm.musiccommunityapp.domain.interactor.discussion.CreateDiscussionUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.CreateEventResult
import com.tfm.musiccommunityapp.domain.interactor.event.CreateEventUseCase
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val getCities: GetCitiesUseCase,
    private val createEvent: CreateEventUseCase,
    private val createAdvertisement: CreateAdvertisementUseCase,
    private val createDiscussion: CreateDiscussionUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class OperationSuccess {
        CREATE_EVENT_SUCCESS,
        CREATE_ADVERTISEMENT_SUCCESS,
        CREATE_DISCUSSION_SUCCESS,
    }

    private val _cities: MutableLiveData<List<CityDomain>> = MutableLiveData()
    private val _operationSuccess: SingleLiveEvent<Pair<OperationSuccess, Long>> = SingleLiveEvent()
    private val _createItemError: SingleLiveEvent<String> = SingleLiveEvent()


    fun getCitiesLiveData() = _cities as LiveData<List<CityDomain>>
    fun getOperationResult() = _operationSuccess as LiveData<Pair<OperationSuccess, Long>>
    fun getCreateItemError() = _createItemError as LiveData<String>

    fun setUpCities() {
        viewModelScope.launch(dispatcher) {
            handleGetCitiesResult(getCities(null))
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

    fun sendCreateEvent(newEvent: EventDomain) {
        viewModelScope.launch(dispatcher) {
            handleCreateEventResult(createEvent(newEvent))
        }
    }

    fun sendCreateAdvertisement(newAdvertisement: AdvertisementDomain) {
        viewModelScope.launch(dispatcher) {
            handleCreateAdvertisementResult(createAdvertisement(newAdvertisement))
        }
    }

    fun sendCreateDiscussion(newDiscussion: DiscussionDomain) {
        viewModelScope.launch(dispatcher) {
            handleCreateDiscussionResult(createDiscussion(newDiscussion))
        }
    }

    private fun handleCreateEventResult(result: CreateEventResult) {
        when (result) {
            is CreateEventResult.Success -> _operationSuccess.postValue(OperationSuccess.CREATE_EVENT_SUCCESS to result.id)
            is CreateEventResult.GenericError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is CreateEventResult.NetworkError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private fun handleCreateAdvertisementResult(result: CreateAdvertisementResult) {
        when (result) {
            is CreateAdvertisementResult.Success -> _operationSuccess.postValue(OperationSuccess.CREATE_ADVERTISEMENT_SUCCESS to result.id)
            is CreateAdvertisementResult.GenericError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is CreateAdvertisementResult.NetworkError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private fun handleCreateDiscussionResult(result: CreateDiscussionResult) {
        when (result) {
            is CreateDiscussionResult.Success -> _operationSuccess.postValue(OperationSuccess.CREATE_DISCUSSION_SUCCESS to result.id)
            is CreateDiscussionResult.GenericError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is CreateDiscussionResult.NetworkError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }
}