@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.view

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
 * Create a [Flow] of view attach events on the [View] instance
 * where the value emitted is one of the 2 event types:
 * [ViewAttachEvent.Attached],
 * [ViewAttachEvent.Detached]
 *
 * This receiver [View] of the extension is the child view being attached / detached.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * childView.attachEvents()
 *     .onEach { event ->
 *          when(event) {
 *              is ViewAttachEvent.Attached -> {
 *                  // handle attached event
 *              }
 *              is ViewAttachEvent.Detached -> {
 *                  // handle detached event
 *              }
 *          }
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun View.attachEvents(): Flow<ViewAttachEvent> =
    callbackFlow<ViewAttachEvent> {
        checkMainThread()
        val listener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                safeOffer(ViewAttachEvent.Attached(v))
            }

            override fun onViewDetachedFromWindow(v: View) {
                safeOffer(ViewAttachEvent.Detached(v))
            }
        }
        addOnAttachStateChangeListener(listener)
        awaitClose { removeOnAttachStateChangeListener(listener) }
    }.conflate()

sealed class ViewAttachEvent {
    abstract val view: View

    class Attached(override val view: View) : ViewAttachEvent()

    class Detached(override val view: View) : ViewAttachEvent()
}
