package work.therock24.albumapp.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}

/**
 * A [ConnectivityObserver] implementation that uses the Android [ConnectivityManager] to observe network connectivity changes.
 *
 * @param context The application context.
 */
@Singleton
class NetworkConnectivityObserver @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val statusFlow = MutableStateFlow(ConnectivityObserver.Status.Unavailable)

    override fun observe(): Flow<ConnectivityObserver.Status> = statusFlow

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            statusFlow.value = ConnectivityObserver.Status.Available
        }

        override fun onLost(network: Network) {
            statusFlow.value = ConnectivityObserver.Status.Lost
        }

        override fun onUnavailable() {
            statusFlow.value = ConnectivityObserver.Status.Unavailable
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            statusFlow.value = ConnectivityObserver.Status.Losing
        }
    }

    init {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)
    }
}
