package io.rhymezxcode.cardinfofinder.util

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import io.github.rhymezxcode.networkstateobserver.network.NetworkObserver
import io.github.rhymezxcode.networkstateobserver.network.NetworkStateObserver
import io.github.rhymezxcode.networkstateobserver.network.Reachability
import io.rhymezxcode.cardinfofinder.data.providers.Url
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import java.io.IOException

object NetworkManager {

    suspend fun callNetworkConnection(
        networkManager: NetworkStateObserver,
        context: Context,
        lifecycle: Lifecycle
    ) {
        networkManager.callNetworkConnectionFlow()
            .observe()
            .collect {
                when (it) {
                    NetworkObserver.Status.Available -> {
                        when {
                            Reachability.hasServerConnectedFlow(
                                context = context,
                                serverUrl = Url.BASE_URL
                            ).retryWhen { cause, attempt ->
                                if (cause is IOException && attempt < 3) {
                                    delay(2000)
                                    return@retryWhen true
                                } else {
                                    return@retryWhen false
                                }
                            }.buffer().first() -> lifecycle.coroutineScope.launch {
//                                        showToast(
//                                            context = context,
//                                            "Server url works"
//                                        )
                            }


                            Reachability.hasInternetConnectedFlow(
                                context = context
                            ).retryWhen { cause, attempt ->
                                if (cause is IOException && attempt < 3) {
                                    delay(2000)
                                    return@retryWhen true
                                } else {
                                    return@retryWhen false
                                }
                            }.buffer().first() -> lifecycle.coroutineScope.launch {
//                                        showToast(
//                                            context = context,
//                                            "Network restored"
//                                        )
                            }

                            else -> lifecycle.coroutineScope.launch {
                                context.showToast(
                                    Constants.SERVER_OR_NETWORK_CONNECTION_MESSAGE
                                )
                            }
                        }
                    }

                    NetworkObserver.Status.Unavailable -> lifecycle.coroutineScope.launch {
                        context.showToast(
                            Constants.NETWORK_UNAVAILABLE_MESSAGE
                        )
                    }

                    NetworkObserver.Status.Losing -> lifecycle.coroutineScope.launch {
                        context.showToast(
                            Constants.LOSING_NETWORK_MESSAGE
                        )
                    }

                    NetworkObserver.Status.Lost -> lifecycle.coroutineScope.launch {
                        context.showToast(
                            Constants.NETWORK_LOST_MESSAGE
                        )
                    }
                }
            }
    }
}