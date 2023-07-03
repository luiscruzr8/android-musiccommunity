package com.tfm.musiccommunityapp.domain.model

sealed class DomainError(
    open val title: String,
    open val message: String,
    open val code: Int,
    open val throwable: Throwable? = null
)

data class AuthError(
    override val title: String,
    override val message: String,
    override val code: Int,
    val cause: Throwable? = null
) : DomainError(title, message, code, cause)

data class NetworkError(
    override val title: String,
    override val message: String,
    override val code: Int,
    val cause: Throwable? = null
) : DomainError(title, message, code, cause)

data class ValidationError(
    override val title: String,
    override val message: String,
    override val code: Int,
    val cause: Throwable? = null
) : DomainError(title, message, code, cause)

data class NotFoundError(
    override val title: String,
    override val message: String,
    override val code: Int,
    val cause: Throwable? = null
) : DomainError(title, message, code, cause)

data class ServerError(
    override val title: String,
    override val message: String,
    override val code: Int,
    val cause: Throwable? = null
) : DomainError(title, message, code, cause)

data class GenericError(
    override val title: String,
    override val message: String,
    override val code: Int,
    val cause: Throwable? = null
) : DomainError(title, message, code, cause)