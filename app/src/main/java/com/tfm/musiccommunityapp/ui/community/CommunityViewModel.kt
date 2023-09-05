package com.tfm.musiccommunityapp.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.advertisement.CreateAdvertisementResult
import com.tfm.musiccommunityapp.usecase.advertisement.CreateAdvertisementUseCase
import com.tfm.musiccommunityapp.usecase.city.GetCitiesResult
import com.tfm.musiccommunityapp.usecase.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.usecase.discussion.CreateDiscussionResult
import com.tfm.musiccommunityapp.usecase.discussion.CreateDiscussionUseCase
import com.tfm.musiccommunityapp.usecase.event.CreateEventResult
import com.tfm.musiccommunityapp.usecase.event.CreateEventUseCase
import com.tfm.musiccommunityapp.usecase.opinion.CreateOpinionResult
import com.tfm.musiccommunityapp.usecase.opinion.CreateOpinionUseCase
import com.tfm.musiccommunityapp.usecase.post.UploadPostImageResult
import com.tfm.musiccommunityapp.usecase.post.UploadPostImageUseCase
import com.tfm.musiccommunityapp.usecase.score.GetUserScoresResult
import com.tfm.musiccommunityapp.usecase.score.GetUserScoresUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File

class CommunityViewModel(
    private val getCities: GetCitiesUseCase,
    private val getMyScores: GetUserScoresUseCase,
    private val createEvent: CreateEventUseCase,
    private val createAdvertisement: CreateAdvertisementUseCase,
    private val createDiscussion: CreateDiscussionUseCase,
    private val createOpinion: CreateOpinionUseCase,
    private val uploadImagePost: UploadPostImageUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class OperationSuccess {
        CREATE_EVENT_SUCCESS,
        CREATE_ADVERTISEMENT_SUCCESS,
        CREATE_DISCUSSION_SUCCESS,
        CREATE_OPINION_SUCCESS,
        IMAGE_UPLOAD_SUCCESS,
    }

    private val _cities: MutableLiveData<List<CityDomain>> = MutableLiveData()
    private val _myScores: MutableLiveData<List<ScoreDomain>> = MutableLiveData()
    private val _operationSuccess: SingleLiveEvent<Pair<OperationSuccess, Long>> = SingleLiveEvent()
    private val _imageUploadSuccess: SingleLiveEvent<OperationSuccess> =
        SingleLiveEvent()
    private val _createItemError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _imageUploadError: SingleLiveEvent<String> = SingleLiveEvent()

    fun getCitiesLiveData() = _cities as LiveData<List<CityDomain>>
    fun getMyScoresLiveData() = _myScores as LiveData<List<ScoreDomain>>
    fun getOperationResult() = _operationSuccess as LiveData<Pair<OperationSuccess, Long>>
    fun getImageResult() = _imageUploadSuccess as LiveData<OperationSuccess>
    fun getCreateItemError() = _createItemError as LiveData<String>
    fun getImageUploadError() = _createItemError as LiveData<String>

    fun setUpCities() {
        viewModelScope.launch(dispatcher) {
            handleGetCitiesResult(getCities(null))
            handleGetMyScoresResult(getMyScores(null, null, false))
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

    private fun handleGetMyScoresResult(result: GetUserScoresResult) {
        when (result) {
            is GetUserScoresResult.Success -> {
                _myScores.postValue(result.scores)
            }

            else -> {
                _myScores.postValue(emptyList())
            }
        }
    }

    fun sendCreateEvent(newEvent: EventDomain) {
        viewModelScope.launch(dispatcher) {
            handleCreateEventResult(
                createEvent(newEvent),
                newEvent.image
            )
        }
    }

    fun sendCreateAdvertisement(newAdvertisement: AdvertisementDomain) {
        viewModelScope.launch(dispatcher) {
            handleCreateAdvertisementResult(
                createAdvertisement(newAdvertisement),
                newAdvertisement.image
            )
        }
    }

    fun sendCreateDiscussion(newDiscussion: DiscussionDomain) {
        viewModelScope.launch(dispatcher) {
            handleCreateDiscussionResult(
                createDiscussion(newDiscussion),
                newDiscussion.image
            )
        }
    }

    fun sendCreateOpinion(newOpinion: OpinionDomain) {
        viewModelScope.launch(dispatcher) {
            handleCreateOpinionResult(createOpinion(newOpinion))
        }
    }

    private suspend fun handleCreateEventResult(
        result: CreateEventResult,
        image: File?
    ) {
        when (result) {
            is CreateEventResult.Success ->
                if (image != null) {
                    handleUploadImageToPost(
                        uploadImagePost(result.id, image),
                        OperationSuccess.CREATE_EVENT_SUCCESS,
                        result.id
                    )
                } else {
                    _operationSuccess.postValue(OperationSuccess.CREATE_EVENT_SUCCESS to result.id)
                }

            is CreateEventResult.GenericError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is CreateEventResult.NetworkError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private suspend fun handleCreateAdvertisementResult(
        result: CreateAdvertisementResult,
        image: File?
    ) {
        when (result) {
            is CreateAdvertisementResult.Success ->
                if (image != null) {
                    handleUploadImageToPost(
                        uploadImagePost(result.id, image),
                        OperationSuccess.CREATE_ADVERTISEMENT_SUCCESS,
                        result.id
                    )
                } else {
                    _operationSuccess.postValue(OperationSuccess.CREATE_ADVERTISEMENT_SUCCESS to result.id)
                }

            is CreateAdvertisementResult.GenericError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is CreateAdvertisementResult.NetworkError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private suspend fun handleCreateDiscussionResult(result: CreateDiscussionResult, image: File?) {
        when (result) {
            is CreateDiscussionResult.Success ->
                if (image != null) {
                    handleUploadImageToPost(
                        uploadImagePost(result.id, image),
                        OperationSuccess.CREATE_DISCUSSION_SUCCESS,
                        result.id
                    )
                } else {
                    _operationSuccess.postValue(OperationSuccess.CREATE_DISCUSSION_SUCCESS to result.id)
                }

            is CreateDiscussionResult.GenericError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is CreateDiscussionResult.NetworkError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private fun handleCreateOpinionResult(result: CreateOpinionResult) {
        when (result) {
            is CreateOpinionResult.Success -> _operationSuccess.postValue(OperationSuccess.CREATE_OPINION_SUCCESS to result.id)
            is CreateOpinionResult.GenericError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is CreateOpinionResult.NetworkError -> _createItemError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private fun handleUploadImageToPost(
        result: UploadPostImageResult,
        operation: OperationSuccess,
        postId: Long
    ) {
        when (result) {
            is UploadPostImageResult.Success -> {
                _imageUploadSuccess.postValue(OperationSuccess.IMAGE_UPLOAD_SUCCESS)
                _operationSuccess.postValue(operation to postId)
            }

            is UploadPostImageResult.GenericError -> _imageUploadError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.NetworkError -> _imageUploadError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.NotFoundError -> _imageUploadError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.ValidationError -> _imageUploadError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }
}