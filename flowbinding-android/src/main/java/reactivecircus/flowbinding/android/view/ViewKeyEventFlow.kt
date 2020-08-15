package reactivecircus.flowbinding.android.view

import android.view.KeyEvent
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
 * Create a [Flow] of key events on the [View] instance.
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [View.OnKeyListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.keys { event.keyCode == KeyEvent.KEYCODE_ENTER }
 *     .onEach { event ->
 *          // handle key event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.keys(handled: (KeyEvent) -> Boolean = { true }): Flow<KeyEvent> = callbackFlow<KeyEvent> {
    checkMainThread()
    val listener = View.OnKeyListener { _, _, event ->
        if (handled(event)) {
            event.keyCode == KeyEvent.KEYCODE_ENTER
            safeOffer(event)
            true
        } else {
            false
        }
    }
    setOnKeyListener(listener)
    awaitClose { setOnKeyListener(null) }
}.conflate()
