package com.tfm.musiccommunityapp.ui.search.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.city.GetCitiesResult
import com.tfm.musiccommunityapp.usecase.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.usecase.post.GetPostByCityResult
import com.tfm.musiccommunityapp.usecase.post.GetPostsByCityUseCase
import com.tfm.musiccommunityapp.usecase.post.GetPostsByCoordinatesResult
import com.tfm.musiccommunityapp.usecase.post.GetPostsByCoordinatesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SearchByCityViewModel(
    private val getCities: GetCitiesUseCase,
    private val searchPostsByCityName: GetPostsByCityUseCase,
    private val searchPostsByCoordinates: GetPostsByCoordinatesUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _posts: MutableLiveData<List<GenericPostDomain>> = MutableLiveData()
    private val _cities: MutableLiveData<List<CityDomain>> = MutableLiveData()
    private val _postsErrors: SingleLiveEvent<String> = SingleLiveEvent()
    private val _citiesErrors: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showLoader: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun getPostsResult() = _posts as LiveData<List<GenericPostDomain>>
    fun getCitiesResult() = _cities as LiveData<List<CityDomain>>
    fun getPostsErrors() = _postsErrors as LiveData<String>
    fun getCitiesErrors() = _citiesErrors as LiveData<String>
    fun isLoading() = _showLoader as LiveData<Boolean>

    fun setUpSearchByCityName(cityName: String?) {
        viewModelScope.launch(dispatcher) {
            _showLoader.postValue(true)
            handleGetCitiesResult(getCities(null))
            if (cityName.isNullOrEmpty()) {
                _posts.postValue(emptyList())
                _showLoader.postValue(false)
            } else {
                handleGetPostsResult(searchPostsByCityName(cityName))
            }
        }
    }

    fun setUpSearchByCoordinates(latitude: Double, longitude: Double, searchClosest: Boolean) {
        viewModelScope.launch(dispatcher){
            _showLoader.postValue(true)
            handleGetPostsResult(searchPostsByCoordinates(latitude, longitude, searchClosest))
        }
    }

    private fun handleGetCitiesResult(result: GetCitiesResult) {
        when (result) {
            is GetCitiesResult.Success ->
                _cities.postValue(result.cities)

            is GetCitiesResult.GenericError ->
                sendCitiesError(result.error.code, result.error.message)

            is GetCitiesResult.NetworkError ->
                sendCitiesError(result.error.code, result.error.message)
        }
    }

    private fun handleGetPostsResult(result: GetPostByCityResult) {
        when (result) {
            is GetPostByCityResult.Success ->
                _posts.postValue(result.posts)

            is GetPostByCityResult.GenericError ->
                sendPostsError(result.error.code, result.error.message)

            is GetPostByCityResult.NetworkError ->
                sendPostsError(result.error.code, result.error.message)
        }
        _showLoader.postValue(false)
    }

    private fun handleGetPostsResult(result: GetPostsByCoordinatesResult) {
        when (result) {
            is GetPostsByCoordinatesResult.Success ->
                _posts.postValue(result.posts)

            is GetPostsByCoordinatesResult.GenericError ->
                sendPostsError(result.error.code, result.error.message)

            is GetPostsByCoordinatesResult.NetworkError ->
                sendPostsError(result.error.code, result.error.message)
        }
        _showLoader.postValue(false)
    }

    private fun sendPostsError(code: Int, message: String) {
        _postsErrors.postValue("Error code: $code - $message")
    }

    private fun sendCitiesError(code: Int, message: String) {
        _citiesErrors.postValue("Error code: $code - $message")
    }

}