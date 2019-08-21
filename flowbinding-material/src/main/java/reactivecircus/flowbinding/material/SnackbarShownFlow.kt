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
 * Create a [Flow] of shown events on the [Snackbar] instance.
 *
 * Note: Created flow keeps a strong reference to the [Snackbar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * snackbar.shownEvents()
 *     .onEach {
 *          // handle shown event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun Snackbar.shownEvents(): Flow<Unit> =
    callbackFlow<Unit> {
        checkMainThread()
        val callback = object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                offerIfNotClosed(Unit)
            }
        }
        addCallback(callback)
        awaitClose { removeCallback(callback) }
    }.conflate()
