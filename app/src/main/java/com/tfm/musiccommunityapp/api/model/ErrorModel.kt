package com.tfm.musiccommunityapp.api.model

import com.tfm.musiccommunityapp.domain.model.AuthError
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.model.ServerError
import com.tfm.musiccommunityapp.domain.model.ValidationError

import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

data class ErrorResponse(
    val code: Int,
    val message: String,
)

fun ErrorResponse.toDomain(): DomainError {
    return when (this.code) {
        HTTP_BAD_REQUEST -> ValidationError(
            title = "Validation Error",
            code = this.code,
            message = this.message
        )
        HTTP_UNAUTHORIZED -> AuthError(
            title = "Authentication Error",
            code = this.code,
            message = this.message
        )
        HTTP_FORBIDDEN -> AuthError(
            title = "Authorization Error",
            code = this.code,
            message = this.message
        )
        HTTP_NOT_FOUND -> NotFoundError(
            title = "Not Found Error",
            code = this.code,
            message = this.message
        )
        HTTP_INTERNAL_ERROR -> ServerError(
            title = "Internal Server Error",
            code = this.code,
            message = this.message
        )
        HTTP_GATEWAY_TIMEOUT -> NetworkError(
            title = "Network Error",
            code = this.code,
            message = this.message
        )
        else -> GenericError(
            title = "Unknown Error",
            code = this.code,
            message = this.message
        )
    }
}