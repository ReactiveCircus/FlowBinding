@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.recyclerview

import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of fling events on the [RecyclerView] instance.
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [RecyclerView.OnFlingListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Created flow keeps a strong reference to the [RecyclerView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * recyclerView.flingEvents { it.velocityX != 0 }
 *     .onEach { event ->
 *          // handle fling event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RecyclerView.flingEvents(handled: (FlingEvent) -> Boolean = { true }): Flow<FlingEvent> =
    callbackFlow<FlingEvent> {
        checkMainThread()
        val listener = object : RecyclerView.OnFlingListener() {
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                val event = FlingEvent(
                    view = this@flingEvents,
                    velocityX = velocityX,
                    velocityY = velocityY
                )
                return if (handled(event)) {
                    safeOffer(event)
                    true
                } else {
                    false
                }
            }
        }
        onFlingListener = listener
        awaitClose { onFlingListener = null }
    }.conflate()

public data class FlingEvent(
    public val view: RecyclerView,
    public val velocityX: Int,
    public val velocityY: Int
)
