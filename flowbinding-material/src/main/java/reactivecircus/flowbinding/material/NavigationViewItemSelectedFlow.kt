package reactivecircus.flowbinding.material

import android.view.MenuItem
import androidx.annotation.CheckResult
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onStart
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of item selected events on the [NavigationView] instance
 * where the value emitted is the currently selected menu item.
 *
 * Note: if a [MenuItem] is already selected, it will be emitted immediately upon collection.
 *
 * Note: Created flow keeps a strong reference to the [NavigationView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * navigationView.itemSelections()
 *     .onEach { menuItem ->
 *          // handle menuItem
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun NavigationView.itemSelections(): Flow<MenuItem> = callbackFlow {
    checkMainThread()
    val listener = NavigationView.OnNavigationItemSelectedListener { item ->
        safeOffer(item)
    }
    setNavigationItemSelectedListener(listener)
    awaitClose { setNavigationItemSelectedListener(null) }
}
    .onStart {
        var selectedItem: MenuItem? = null
        for (index in 0 until menu.size()) {
            val item = menu.getItem(index)
            if (item.isChecked) {
                selectedItem = item
                break
            }
        }
        selectedItem?.run { emit(this) }
    }
    .conflate()
