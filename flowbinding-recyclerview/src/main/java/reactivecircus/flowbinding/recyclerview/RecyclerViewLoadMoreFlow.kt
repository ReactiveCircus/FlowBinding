package reactivecircus.flowbinding.recyclerview

import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of scroll events on the [RecyclerView] instance.
 *
 * Note: create a extension for loading more in different directions
 *
 * Example of usage:
 *
 * ```
 * recyclerView.loadMoreFlowVertically()
 *     .onEach { loadMore ->
 *          // handle data changed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RecyclerView.loadMoreFlowVertically(): Flow<Boolean> = callbackFlow {
    checkMainThread()
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                trySend(true)
            } else {
                trySend(false)
            }
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}.conflate()


@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun RecyclerView.loadMoreFlowHorizontally(): Flow<Boolean> = callbackFlow {
    checkMainThread()
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (!recyclerView.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                trySend(true)
            } else {
                trySend(false)
            }
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}.conflate()