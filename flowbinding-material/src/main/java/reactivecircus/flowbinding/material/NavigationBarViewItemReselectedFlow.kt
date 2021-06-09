package reactivecircus.flowbinding.material

import android.view.MenuItem
import androidx.annotation.CheckResult
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of item reselected events on the [NavigationBarView] instance
 * where the value emitted is the currently selected menu item.
 *
 * Note: Created flow keeps a strong reference to the [NavigationBarView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * bottomNavigationView.itemReselections()
 *     .onEach { menuItem ->
 *          // handle menuItem
 *     }
 *     .launchIn(uiScope)
 *
 * navigationRailView.itemReselections()
 *     .onEach { menuItem ->
 *          // handle menuItem
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun NavigationBarView.itemReselections(): Flow<MenuItem> = callbackFlow {
    checkMainThread()
    val listener = NavigationBarView.OnItemReselectedListener { item ->
        trySend(item)
    }
    setOnItemReselectedListener(listener)
    awaitClose { setOnItemReselectedListener(null) }
}.conflate()
