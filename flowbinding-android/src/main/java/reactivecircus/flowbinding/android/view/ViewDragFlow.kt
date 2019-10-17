package reactivecircus.flowbinding.android.view

import android.view.DragEvent
import android.view.View
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of drag events on the [View] instance.
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [View.OnDragListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.drags { it.action == DragEvent.ACTION_DRAG_ENDED }
 *     .onEach { event ->
 *          // handle drag event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun View.drags(handled: (DragEvent) -> Boolean = { true }): Flow<DragEvent> = callbackFlow {
    checkMainThread()
    val listener = View.OnDragListener { _, event ->
        if (handled(event)) {
            safeOffer(event)
            true
        } else {
            false
        }
    }
    setOnDragListener(listener)
    awaitClose { setOnDragListener(null) }
}.conflate()
