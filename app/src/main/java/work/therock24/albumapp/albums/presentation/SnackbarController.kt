package work.therock24.albumapp.albums.presentation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.annotations.VisibleForTesting

sealed class SnackbarEventType {
    data object Offline : SnackbarEventType()
    data object Dismiss : SnackbarEventType()
}

data class SnackbarEvent(
    val type: SnackbarEventType,
    val action: suspend () -> Unit
)
/**
 * A controller for managing Snackbar events.
 *
 * This class uses a [Channel] to send events to the UI layer, which can then display
 * a Snackbar with the provided message and action.
 */
object SnackbarController {

    private var _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    // For test purposes: allow replacing the event channel
    @VisibleForTesting
    fun overrideEvents(channel: Channel<SnackbarEvent>) {
        _events = channel
    }
}