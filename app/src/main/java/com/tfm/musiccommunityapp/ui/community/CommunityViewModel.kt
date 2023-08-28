package com.tfm.musiccommunityapp.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.advertisement.CreateAdvertisementUseCase
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesResult
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.CreateDiscussionUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.CreateEventUseCase
import com.tfm.musiccommunityapp.domain.model.CityDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val getCities: GetCitiesUseCase,
    private val createEvent: CreateEventUseCase,
    private val createAdvertisement: CreateAdvertisementUseCase,
    private val createDiscussion: CreateDiscussionUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _cities: MutableLiveData<List<CityDomain>> = MutableLiveData()

    fun getCitiesLiveData() = _cities as LiveData<List<CityDomain>>

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
}