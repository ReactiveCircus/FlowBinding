@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.recyclerview

import android.view.View
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of child attach state change events on the [RecyclerView] instance
 * where the value emitted is one of the 2 event types:
 * [RecyclerViewChildAttachStateChangeEvent.Attached],
 * [RecyclerViewChildAttachStateChangeEvent.Detached]
 *
 * Note: Created flow keeps a strong reference to the [RecyclerView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * recyclerView.childAttachStateChangeEvents()
 *     .onEach { event ->
 *          when(event) {
 *              is RecyclerViewChildAttachStateChangeEvent.Attached -> {
 *                  // handle attached event
 *              }
 *              is RecyclerViewChildAttachStateChangeEvent.Detached -> {
 *                  // handle detached event
 *              }
 *          }
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RecyclerView.childAttachStateChangeEvents(): Flow<RecyclerViewChildAttachStateChangeEvent> = callbackFlow {
    checkMainThread()
    val listener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(childView: View) {
            trySend(
                RecyclerViewChildAttachStateChangeEvent.Attached(
                    view = this@childAttachStateChangeEvents,
                    child = childView
                )
            )
        }

        override fun onChildViewDetachedFromWindow(childView: View) {
            trySend(
                RecyclerViewChildAttachStateChangeEvent.Detached(
                    view = this@childAttachStateChangeEvents,
                    child = childView
                )
            )
        }
    }
    addOnChildAttachStateChangeListener(listener)
    awaitClose { removeOnChildAttachStateChangeListener(listener) }
}.conflate()

public sealed class RecyclerViewChildAttachStateChangeEvent {
    public abstract val view: RecyclerView
    public abstract val child: View

    public data class Attached(
        override val view: RecyclerView,
        override val child: View
    ) : RecyclerViewChildAttachStateChangeEvent()

    public data class Detached(
        override val view: RecyclerView,
        override val child: View
    ) : RecyclerViewChildAttachStateChangeEvent()
}
