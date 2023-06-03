package com.tfm.musiccommunityapp.data.api.extensions

import com.tfm.musiccommunityapp.data.api.model.ErrorResponse
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

const val DEFAULT_ERROR_CODE = 999
const val DEFAULT_ERROR_401_MESSAGE = "Unauthorized error"
const val DEFAULT_ERROR_403_MESSAGE = "Forbidden error"
const val DEFAULT_ERROR_400_MESSAGE = "Bad request error"
const val DEFAULT_ERROR_404_MESSAGE = "Not found error"
const val DEFAULT_ERROR_500_MESSAGE = "Internal server error"
const val DEFAULT_ERROR_504_MESSAGE = "Gateway timeout error"
const val DEFAULT_ERROR_MESSAGE = "Unknown error"

fun <T> Response<T>.toErrorResponse(): ErrorResponse =
    when (this.code()) {
        HTTP_UNAUTHORIZED -> ErrorResponse(code = HTTP_UNAUTHORIZED, message = this.message() ?: DEFAULT_ERROR_401_MESSAGE)
        HTTP_FORBIDDEN -> ErrorResponse(code = HTTP_FORBIDDEN, message = this.message() ?: DEFAULT_ERROR_403_MESSAGE)
        HTTP_BAD_REQUEST -> ErrorResponse(code = HTTP_BAD_REQUEST, message = this.message() ?: DEFAULT_ERROR_400_MESSAGE)
        HTTP_NOT_FOUND -> ErrorResponse(code = HTTP_NOT_FOUND, message = this.message() ?:  DEFAULT_ERROR_404_MESSAGE)
        HTTP_INTERNAL_ERROR -> ErrorResponse(code = HTTP_INTERNAL_ERROR, message = this.message() ?: DEFAULT_ERROR_500_MESSAGE)
        HTTP_GATEWAY_TIMEOUT -> ErrorResponse(code = HTTP_GATEWAY_TIMEOUT, message = this.message() ?: DEFAULT_ERROR_504_MESSAGE)
        else -> ErrorResponse(code = DEFAULT_ERROR_CODE, message = this.message() ?: DEFAULT_ERROR_MESSAGE)
    }