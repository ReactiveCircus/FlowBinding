@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of tab selection events on the [TabLayout] instance
 * where the value emitted is one of the 3 event types:
 * [TabLayoutSelectionEvent.TabSelected],
 * [TabLayoutSelectionEvent.TabReselected],
 * [TabLayoutSelectionEvent.TabUnselected]
 *
 * @param emitImmediately whether to emit TabLayoutSelectionEvent.TabSelected immediately
 *  if a tab is already selected
 *
 * Note: Created flow keeps a strong reference to the [TabLayout] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * // observe all 3 types of events
 * tabLayout.tabSelectionEvents()
 *      .onEach { selectedEvent ->
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
@UseExperimental(ExperimentalCoroutinesApi::class)
fun TabLayout.tabSelectionEvents(emitImmediately: Boolean = false): Flow<TabLayoutSelectionEvent> =
    callbackFlow<TabLayoutSelectionEvent> {
        checkMainThread()
        val listener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                safeOffer(TabLayoutSelectionEvent.TabSelected(this@tabSelectionEvents, tab))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                safeOffer(TabLayoutSelectionEvent.TabReselected(this@tabSelectionEvents, tab))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                safeOffer(TabLayoutSelectionEvent.TabUnselected(this@tabSelectionEvents, tab))
            }
        }
        addOnTabSelectedListener(listener)
        awaitClose { removeOnTabSelectedListener(listener) }
    }
        .startWithCurrentValue(emitImmediately) {
            // emit TabLayoutSelectionEvent.TabSelected if a tab is already selected
            getTabAt(selectedTabPosition)?.let {
                TabLayoutSelectionEvent.TabSelected(this@tabSelectionEvents, it)
            }
        }
        .conflate()

sealed class TabLayoutSelectionEvent {
    abstract val tabLayout: TabLayout
    abstract val tab: TabLayout.Tab

    data class TabSelected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()

    data class TabReselected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()

    data class TabUnselected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()
}
