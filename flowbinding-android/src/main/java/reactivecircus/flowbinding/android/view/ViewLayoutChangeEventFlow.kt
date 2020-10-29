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
 * Create a [Flow] of view layout change events on the [View] instance.
 *
 * Note: Created flow keeps a strong reference to the [View] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * view.layoutChangeEvents()
 *     .onEach { event ->
 *          // handle layout change event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun View.layoutChangeEvents(): Flow<LayoutChangeEvent> = callbackFlow {
    checkMainThread()
    val listener =
        View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            safeOffer(
                LayoutChangeEvent(
                    view = v,
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom,
                    oldLeft = oldLeft,
                    oldTop = oldTop,
                    oldRight = oldRight,
                    oldBottom = oldBottom
                )
            )
        }
    addOnLayoutChangeListener(listener)
    awaitClose { removeOnLayoutChangeListener(listener) }
}.conflate()

@Suppress("LongParameterList")
public data class LayoutChangeEvent(
    public val view: View,
    public val left: Int,
    public val top: Int,
    public val right: Int,
    public val bottom: Int,
    public val oldLeft: Int,
    public val oldTop: Int,
    public val oldRight: Int,
    public val oldBottom: Int
)
