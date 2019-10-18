@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.view

import android.os.Build
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of view scroll change events on the [View] instance.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.scrollChangeEvents()
 *     .onEach { event ->
 *          // handle scroll change event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@RequiresApi(Build.VERSION_CODES.M)
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun View.scrollChangeEvents(): Flow<ScrollChangeEvent> = callbackFlow {
    checkMainThread()
    val listener = View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        safeOffer(
            ScrollChangeEvent(
                view = v,
                scrollX = scrollX,
                scrollY = scrollY,
                oldScrollX = oldScrollX,
                oldScrollY = oldScrollY
            )
        )
    }
    setOnScrollChangeListener(listener)
    awaitClose { setOnScrollChangeListener(listener) }
}.conflate()

class ScrollChangeEvent(
    val view: View,
    val scrollX: Int,
    val scrollY: Int,
    val oldScrollX: Int,
    val oldScrollY: Int
)
