package com.tfm.musiccommunityapp.api.network.interceptors

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class BackendInterceptor(private val localAuth: AuthDatasource): Interceptor {

    companion object {
        const val HEADER_AUTH = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val authUrl = chain.request().url.let { _ ->
            "${BuildConfig.BACKEND_URL}auth/signin"
        }

        val request = chain.request()

        return try {
            when (request.url.toString()) {
                authUrl -> chain.proceed(request) //Ignore auth requests, they are treated in repository
                else -> handleRequest(chain)
            }
        } catch (e: SocketTimeoutException) {
            Response.Builder()
                .code(999)
                .request(chain.request())
                .body(buildErrorBody(chain.request(), e))
                .protocol(Protocol.HTTP_1_1)
                .message("Error: ${e.message}")
                .build()
        } catch (e: UnknownHostException) {
            Response.Builder()
                .code(999)
                .request(chain.request())
                .body(buildErrorBody(chain.request(), e))
                .protocol(Protocol.HTTP_1_1)
                .message("Error: ${e.message}")
                .build()
        }
    }

    private fun buildErrorBody(request: Request, exception: IOException) =
        JSONObject().apply {
            put("title", exception::class.java.simpleName)
            put("exception", exception.toJsonString())
            put("request", request.toJsonString())
        }.toString().toResponseBody(null)

    private fun Response.toJsonString() = "${code}-${isSuccessful} : $message"
    private fun Request.toJsonString() = "$method :  ${url.pathSegments.joinToString("/")}"

    private fun IOException.toJsonString() = "$message : $cause"

    private fun handleRequest(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .header(
                HEADER_AUTH,
                localAuth.bearerToken
            )
        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}