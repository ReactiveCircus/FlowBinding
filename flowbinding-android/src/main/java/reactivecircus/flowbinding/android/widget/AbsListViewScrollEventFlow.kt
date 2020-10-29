@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.widget.AbsListView
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of scroll events on the [AbsListView] instance.
 *
 * Note: Created flow keeps a strong reference to the [AbsListView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * absListView.scrollEvents()
 *     .onEach { event ->
 *          // handle scroll event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun AbsListView.scrollEvents(): Flow<ScrollEvent> = callbackFlow<ScrollEvent> {
    checkMainThread()
    val listener = object : AbsListView.OnScrollListener {

        private var currentScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE

        override fun onScrollStateChanged(absListView: AbsListView, scrollState: Int) {
            currentScrollState = scrollState
            safeOffer(
                ScrollEvent(
                    view = this@scrollEvents,
                    scrollState = scrollState,
                    firstVisibleItem = firstVisiblePosition,
                    visibleItemCount = childCount,
                    totalItemCount = count
                )
            )
        }

        override fun onScroll(
            absListView: AbsListView,
            firstVisibleItem: Int,
            visibleItemCount: Int,
            totalItemCount: Int
        ) {
            safeOffer(
                ScrollEvent(
                    view = this@scrollEvents,
                    scrollState = currentScrollState,
                    firstVisibleItem = firstVisibleItem,
                    visibleItemCount = visibleItemCount,
                    totalItemCount = totalItemCount
                )
            )
        }
    }
    setOnScrollListener(listener)
    awaitClose { setOnScrollListener(null) }
}.conflate()

public data class ScrollEvent(
    public val view: AbsListView,
    public val scrollState: Int,
    public val firstVisibleItem: Int,
    public val visibleItemCount: Int,
    public val totalItemCount: Int
)
