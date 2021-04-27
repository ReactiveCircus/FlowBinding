package reactivecircus.flowbinding.swiperefreshlayout

import androidx.annotation.CheckResult
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of refresh events on the [SwipeRefreshLayout] instance.
 *
 * Note: Created flow keeps a strong reference to the [SwipeRefreshLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * swipeRefreshLayout.refreshes()
 *     .onEach {
 *          // handle refreshed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun SwipeRefreshLayout.refreshes(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = SwipeRefreshLayout.OnRefreshListener {
        trySend(Unit)
    }
    setOnRefreshListener(listener)
    awaitClose { setOnRefreshListener(null) }
}.conflate()
