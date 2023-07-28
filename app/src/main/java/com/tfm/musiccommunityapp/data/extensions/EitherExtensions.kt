package com.tfm.musiccommunityapp.data.extensions

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.tfm.musiccommunityapp.domain.model.DomainError
import retrofit2.Response


suspend fun <T, R> eitherOf(request: suspend () -> Response<T>, mapper: (T?) -> R, errorMapper : (Response<T>) -> DomainError): Either<DomainError, R> =
    eitherOf(request(), mapper, errorMapper)

fun <T, R> eitherOf(response: Response<T>, mapper: (T?) -> R, errorMapper : (Response<T>) -> DomainError): Either<DomainError, R> =
     response.toEither(mapper, errorMapper)

private fun <T, R> Response<T>.toEither(mapper: (T?) -> R, errorMapper : (Response<T>) -> DomainError): Either<DomainError, R> = this.let { response ->
    response.body()?.let {
        mapper(it).right()
    } ?: errorMapper(response).left()
}