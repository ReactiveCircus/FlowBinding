package reactivecircus.flowbinding.material

import android.view.MenuItem
import androidx.annotation.CheckResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of item selected events on the [BottomNavigationView] instance
 * where the value emitted is the currently selected menu item.
 *
 * Note: initial value will only be emitted upon collection if a [MenuItem] is selected.
 *
 * Note: Created flow keeps a strong reference to the [BottomNavigationView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * bottomNavigationView.itemSelections()
 *     .onEach { menuItem ->
 *          // handle menuItem
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun BottomNavigationView.itemSelections(): InitialValueFlow<MenuItem> = callbackFlow {
    checkMainThread()
    val listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        safeOffer(item)
    }
    setOnNavigationItemSelectedListener(listener)
    awaitClose { setOnNavigationItemSelectedListener(null) }
}
    .conflate()
    .asInitialValueFlow {
        var selectedItem: MenuItem? = null
        for (index in 0 until menu.size()) {
            val item = menu.getItem(index)
            if (item.isChecked) {
                selectedItem = item
                break
            }
        }
        selectedItem
    }
