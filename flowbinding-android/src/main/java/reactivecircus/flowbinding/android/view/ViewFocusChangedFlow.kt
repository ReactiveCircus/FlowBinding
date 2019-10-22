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
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of focus changed events on the [View] instance,
 * where the value emitted indicates whether the [View] has focus.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.focusChanges()
 *     .onEach {
 *          // handle view focus changed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun View.focusChanges(emitImmediately: Boolean = false): Flow<Boolean> = callbackFlow {
    checkMainThread()
    val listener = View.OnFocusChangeListener { _, hasFocus ->
        safeOffer(hasFocus)
    }
    onFocusChangeListener = listener
    awaitClose { onFocusChangeListener = null }
}
    .startWithCurrentValue(emitImmediately) { hasFocus() }
    .conflate()
