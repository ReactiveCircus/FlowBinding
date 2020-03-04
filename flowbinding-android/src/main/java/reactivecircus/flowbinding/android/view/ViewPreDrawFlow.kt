package reactivecircus.flowbinding.android.view

import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of pre-draw events on the [View] instance.
 *
 * @param proceedDrawingPass function to be invoked to determine
 * whether to proceed with the current drawing pass or cancel.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.preDraws { shouldProceedDrawingPass }
 *     .onEach {
 *          // handle pre-draw
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun View.preDraws(proceedDrawingPass: () -> Boolean): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = ViewTreeObserver.OnPreDrawListener {
        safeOffer(Unit)
        proceedDrawingPass()
    }
    viewTreeObserver.addOnPreDrawListener(listener)
    awaitClose { viewTreeObserver.removeOnPreDrawListener(listener) }
}.conflate()
