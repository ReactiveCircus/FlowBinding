@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.core

import android.view.View
import androidx.annotation.CheckResult
import androidx.core.widget.NestedScrollView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of scroll change events on the [NestedScrollView] instance.
 *
 * Note: Created flow keeps a strong reference to the [NestedScrollView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * nestedScrollView.scrollChangeEvents()
 *     .onEach { event ->
 *          // handle scroll change event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun NestedScrollView.scrollChangeEvents(): Flow<ScrollChangeEvent> = callbackFlow {
    checkMainThread()
    val listener = NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
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
    awaitClose { setOnScrollChangeListener(null as NestedScrollView.OnScrollChangeListener?) }
}.conflate()

class ScrollChangeEvent(
    val view: View,
    val scrollX: Int,
    val scrollY: Int,
    val oldScrollX: Int,
    val oldScrollY: Int
)
