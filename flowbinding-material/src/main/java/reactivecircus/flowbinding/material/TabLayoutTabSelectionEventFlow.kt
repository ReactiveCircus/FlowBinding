@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.material

import androidx.annotation.CheckResult
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of tab selection events on the [TabLayout] instance
 * where the value emitted is one of the 3 event types:
 * [TabLayoutSelectionEvent.TabSelected],
 * [TabLayoutSelectionEvent.TabReselected],
 * [TabLayoutSelectionEvent.TabUnselected]
 *
 * Note: [TabLayoutSelectionEvent.TabSelected] will only be emitted upon collection if a a tab is already selected.
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
fun TabLayout.tabSelectionEvents(): InitialValueFlow<TabLayoutSelectionEvent> =
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
        .conflate()
        .asInitialValueFlow {
            // emit TabLayoutSelectionEvent.TabSelected if a tab is already selected
            getTabAt(selectedTabPosition)?.let {
                TabLayoutSelectionEvent.TabSelected(this@tabSelectionEvents, it)
            }
        }

sealed class TabLayoutSelectionEvent {
    abstract val tabLayout: TabLayout
    abstract val tab: TabLayout.Tab

    class TabSelected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()

    class TabReselected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()

    class TabUnselected(
        override val tabLayout: TabLayout,
        override val tab: TabLayout.Tab
    ) : TabLayoutSelectionEvent()
}
