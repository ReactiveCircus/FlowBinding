package reactivecircus.flowbinding.android.view

import android.view.View
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] which emits on view layout changes.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.layoutChanges()
 *     .onEach {
 *          // handle layout changed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.layoutChanges(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener =
        View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            trySend(Unit)
        }
    addOnLayoutChangeListener(listener)
    awaitClose { removeOnLayoutChangeListener(listener) }
}.conflate()
