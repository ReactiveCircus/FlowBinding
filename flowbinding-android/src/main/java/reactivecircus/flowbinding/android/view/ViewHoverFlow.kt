package reactivecircus.flowbinding.android.view

import android.view.MotionEvent
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
 * Create a [Flow] of hover events on the [View] instance.
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [View.OnHoverListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Values emitted by this flow are **mutable** and part of a shared
 * object pool and thus are **not safe** to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls [MotionEvent.obtain] or
 * [MotionEvent.obtainNoHistory] to create a copy.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.hovers { event.action == MotionEvent.ACTION_HOVER_ENTER }
 *     .onEach { event ->
 *          // handle hover event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun View.hovers(handled: (MotionEvent) -> Boolean = { true }): Flow<MotionEvent> =
    callbackFlow<MotionEvent> {
        checkMainThread()
        val listener = View.OnHoverListener { _, event ->
            if (handled(event)) {
                safeOffer(event)
                true
            } else {
                false
            }
        }
        setOnHoverListener(listener)
        awaitClose { setOnHoverListener(null) }
    }.conflate()
