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
 * Create a [Flow] of system UI visibility change events on the [View] instance
 * where the value emitted is one of the following:
 * [View.SYSTEM_UI_FLAG_LOW_PROFILE],
 * [View.SYSTEM_UI_FLAG_HIDE_NAVIGATION],
 * [View.SYSTEM_UI_FLAG_FULLSCREEN]
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.systemUiVisibilityChanges()
 *     .onEach { event
 *          when(event) {
 *              View.SYSTEM_UI_FLAG_LOW_PROFILE -> {
 *                  // handle system UI low profile event
 *              }
 *              View.SYSTEM_UI_FLAG_HIDE_NAVIGATION -> {
 *                  // handle system UI hide navigation event
 *              }
 *              View.SYSTEM_UI_FLAG_FULLSCREEN -> {
 *                  // handle system UI fullscreen event
 *              }
 *          }
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun View.systemUiVisibilityChanges(): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = View.OnSystemUiVisibilityChangeListener { flag ->
        safeOffer(flag)
    }
    setOnSystemUiVisibilityChangeListener(listener)
    awaitClose { setOnSystemUiVisibilityChangeListener(null) }
}.conflate()
