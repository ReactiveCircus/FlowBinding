package reactivecircus.flowbinding.material

import android.view.MenuItem
import androidx.annotation.CheckResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of item reselected events on the [BottomNavigationView] instance
 * where the value emitted is the currently selected menu item.
 *
 * Note: Created flow keeps a strong reference to the [BottomNavigationView] instance
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
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BottomNavigationView.itemReselections(): Flow<MenuItem> = callbackFlow {
    checkMainThread()
    val listener = BottomNavigationView.OnNavigationItemReselectedListener { item ->
        safeOffer(item)
    }
    setOnNavigationItemReselectedListener(listener)
    awaitClose { setOnNavigationItemReselectedListener(null) }
}.conflate()
