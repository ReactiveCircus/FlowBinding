@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onStart
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of tab selection events on the [TabLayout] instance
 * where the value emitted is one of the 3 event types:
 * [TabLayoutSelectionEvent.TabSelected],
 * [TabLayoutSelectionEvent.TabReselected],
 * [TabLayoutSelectionEvent.TabUnselected]
 *
 * Note: if a a tab is already selected,
 * [TabLayoutSelectionEvent.TabSelected] will be emitted immediately upon collection.
 *
 * Note: Created flow keeps a strong reference to the [TabLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * // observe all 3 types of events
 * tabLayout.tabSelectionEvents()
 *      .onEach { event ->
 *          when(event) {
 *              is TabLayoutSelectionEvent.TabSelected -> {
 *                  // handle selected event
 *              }
 *              is TabLayoutSelectionEvent.TabReselected -> {
 *                  // handle reselected event
 *              }
 *              is TabLayoutSelectionEvent.TabUnselected -> {
 *                  // handle unselected event
 *              }
 *          }
 *      }
 *     .launchIn(uiScope)
 * ```
 *
 * // only observe 1 type of events
 * tabLayout.tabSelectionEvents()
 *      .filterIsInstance<TabLayoutSelectionEvent.TabSelected>()
 *      .onEach { event ->
 *          // handle selected event
 *      }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun TabLayout.tabSelectionEvents(): Flow<TabLayoutSelectionEvent> = callbackFlow {
    checkMainThread()
    val listener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            trySend(TabLayoutSelectionEvent.TabSelected(this@tabSelectionEvents, tab))
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            trySend(TabLayoutSelectionEvent.TabReselected(this@tabSelectionEvents, tab))
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            trySend(TabLayoutSelectionEvent.TabUnselected(this@tabSelectionEvents, tab))
        }
    }
    addOnTabSelectedListener(listener)
    awaitClose { removeOnTabSelectedListener(listener) }
}
    .onStart {
        // emit TabLayoutSelectionEvent.TabSelected if a tab is already selected
        getTabAt(selectedTabPosition)?.run {
            emit(TabLayoutSelectionEvent.TabSelected(this@tabSelectionEvents, this))
        }
    }
    .conflate()

public sealed class TabLayoutSelectionEvent {
    public abstract val tabLayout: TabLayout
    public abstract val tab: TabLayout.Tab

    public data class TabSelected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()

    public data class TabReselected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()

    public data class TabUnselected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()
}
