package com.tfm.musiccommunityapp.api.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface NetworkTracker {
    val isNetworkAvailable: Boolean
    val isNetworkAvailableFlow: Flow<Boolean>

    suspend fun isConnected(): Boolean
}

class NetworkTrackerImpl(context: Context): NetworkTracker {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()
        ?: throw IllegalStateException("ConnectivityManager not found")

    private val networkCallback = object: ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            networkAvailableMutableFlow.value =
                connectivityManager.getNetworkCapabilities(network)?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) ?: false
        }

        override fun onUnavailable() {
            super.onUnavailable()
            networkAvailableMutableFlow.value = false
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            networkAvailableMutableFlow.value = false
        }
    }

    private val networkAvailableMutableFlow = MutableStateFlow(false)

    override val isNetworkAvailable: Boolean
        get() = networkAvailableMutableFlow.value

    override val isNetworkAvailableFlow: Flow<Boolean>
        get() = networkAvailableMutableFlow

    init {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override suspend fun isConnected(): Boolean = networkAvailableMutableFlow.value
}