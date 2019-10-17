@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.view

import android.view.MenuItem
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of action view events on the [MenuItem] instance
 * where the value emitted is one of the 2 event types:
 * [MenuItemActionViewEvent.Collapse],
 * [MenuItemActionViewEvent.Expand]
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [MenuItem.OnActionExpandListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Created flow keeps a strong reference to the [MenuItem] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * menuItem.actionViewEvents { it.menuItem.isChecked }
 *     .onEach { event ->
 *          when(event) {
 *              is MenuItemActionViewEvent.Collapse -> {
 *                  // handle collapse event
 *              }
 *              is MenuItemActionViewEvent.Expand -> {
 *                  // handle expand event
 *              }
 *          }
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun MenuItem.actionViewEvents(handled: (MenuItemActionViewEvent) -> Boolean = { true }): Flow<MenuItemActionViewEvent> =
    callbackFlow<MenuItemActionViewEvent> {
        checkMainThread()
        val listener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                val event = MenuItemActionViewEvent.Expand(item)
                return if (handled(event)) {
                    safeOffer(event)
                    true
                } else {
                    false
                }
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                val event = MenuItemActionViewEvent.Collapse(item)
                return if (handled(event)) {
                    safeOffer(event)
                    true
                } else {
                    false
                }
            }
        }
        setOnActionExpandListener(listener)
        awaitClose { setOnActionExpandListener(null) }
    }.conflate()

sealed class MenuItemActionViewEvent {
    abstract val menuItem: MenuItem

    class Collapse(
        override val menuItem: MenuItem
    ) : MenuItemActionViewEvent()

    class Expand(
        override val menuItem: MenuItem
    ) : MenuItemActionViewEvent()
}
