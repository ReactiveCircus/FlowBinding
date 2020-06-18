package reactivecircus.flowbinding.android.view

import android.view.View
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of focus changed events on the [View] instance,
 * where the value emitted indicates whether the [View] has focus.
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
@OptIn(ExperimentalCoroutinesApi::class)
fun View.focusChanges(): InitialValueFlow<Boolean> = callbackFlow {
    checkMainThread()
    val listener = View.OnFocusChangeListener { _, hasFocus ->
        safeOffer(hasFocus)
    }
    onFocusChangeListener = listener
    awaitClose { onFocusChangeListener = null }
}
    .conflate()
    .asInitialValueFlow { hasFocus() }
