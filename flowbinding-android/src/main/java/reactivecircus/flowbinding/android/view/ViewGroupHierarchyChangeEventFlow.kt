@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of view group hierarchy change events on the [ViewGroup] instance
 * where the value emitted is one of the 2 event types:
 * [HierarchyChangeEvent.ChildAdded],
 * [HierarchyChangeEvent.ChildRemoved]
 *
 * Note: Created flow keeps a strong reference to the [ViewGroup] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * viewGroup.hierarchyChangeEvents()
 *     .onEach { event ->
 *          when(event) {
 *              is HierarchyChangeEvent.ChildAdded -> {
 *                  // handle child added event
 *              }
 *              is HierarchyChangeEvent.ChildRemoved -> {
 *                  // handle child removed event
 *              }
 *          }
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun ViewGroup.hierarchyChangeEvents(): Flow<HierarchyChangeEvent> =
    callbackFlow<HierarchyChangeEvent> {
        checkMainThread()
        val listener = object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View, child: View) {
                safeOffer(HierarchyChangeEvent.ChildAdded(this@hierarchyChangeEvents, child))
            }

            override fun onChildViewRemoved(parent: View, child: View) {
                safeOffer(HierarchyChangeEvent.ChildRemoved(this@hierarchyChangeEvents, child))
            }
        }
        setOnHierarchyChangeListener(listener)
        awaitClose { setOnHierarchyChangeListener(null) }
    }.conflate()

sealed class HierarchyChangeEvent {
    abstract val parent: ViewGroup
    abstract val child: View

    class ChildAdded(override val parent: ViewGroup, override val child: View) : HierarchyChangeEvent()

    class ChildRemoved(override val parent: ViewGroup, override val child: View) : HierarchyChangeEvent()
}
