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
 * Create a [Flow] of scroll change events on the [View] instance.
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.scrollChangeEvents(): Flow<ScrollChangeEvent> = callbackFlow {
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
    awaitClose { setOnScrollChangeListener(null) }
}.conflate()

public data class ScrollChangeEvent(
    public val view: View,
    public val scrollX: Int,
    public val scrollY: Int,
    public val oldScrollX: Int,
    public val oldScrollY: Int
)
