package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.offerIfNotClosed

/**
 * Create a [Flow] of dismiss events on the [Snackbar] instance,
 * where the value emitted can be one of the DISMISS_EVENT_* events
 * from [com.google.android.material.snackbar.Snackbar.Callback].
 *
 * Note: Created flow keeps a strong reference to the [Snackbar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * snackbar.dismissEvents()
 *     .onEach { event ->
 *          // handle dismiss event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun Snackbar.dismissEvents(): Flow<Int> =
    callbackFlow<Int> {
        checkMainThread()
        val callback = object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                offerIfNotClosed(event)
            }
        }
        addCallback(callback)
        awaitClose { removeCallback(callback) }
    }.conflate()
