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
 * Create a [Flow] of scroll events on the [RecyclerView] instance.
 *
 * Note: Created flow keeps a strong reference to the [RecyclerView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * recyclerView.scrollEvents()
 *     .onEach { event ->
 *          // handle recycler view scroll event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun RecyclerView.scrollEvents(): Flow<RecyclerViewScrollEvent> = callbackFlow<RecyclerViewScrollEvent> {
    checkMainThread()
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            safeOffer(
                RecyclerViewScrollEvent(
                    view = this@scrollEvents,
                    dx = dx,
                    dy = dy
                )
            )
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}.conflate()

class RecyclerViewScrollEvent(
    val view: RecyclerView,
    val dx: Int,
    val dy: Int
)
