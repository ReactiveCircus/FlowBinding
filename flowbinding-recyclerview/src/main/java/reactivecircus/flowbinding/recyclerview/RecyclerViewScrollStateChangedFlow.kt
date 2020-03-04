package reactivecircus.flowbinding.recyclerview

import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of scroll state change events on the [RecyclerView] instance
 * where the value emitted can be one of [SCROLL_STATE_IDLE], [SCROLL_STATE_DRAGGING]} or [SCROLL_STATE_SETTLING].
 *
 * Note: Created flow keeps a strong reference to the [RecyclerView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * recyclerView.scrollStateChanges()
 *     .onEach { state ->
 *          // handle state
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun RecyclerView.scrollStateChanges(): Flow<Int> = callbackFlow<Int> {
    checkMainThread()
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            safeOffer(newState)
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}.conflate()
