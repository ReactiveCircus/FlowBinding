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
 * Create a [Flow] of click events on the [MenuItem] instance.
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [MenuItem.OnMenuItemClickListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Created flow keeps a strong reference to the [MenuItem] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * menuItem.clicks { it.isChecked }
 *     .onEach {
 *          // handle menu item clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun MenuItem.clicks(handled: (MenuItem) -> Boolean = { true }): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = MenuItem.OnMenuItemClickListener {
        if (handled(it)) {
            safeOffer(Unit)
            true
        } else {
            false
        }
    }
    setOnMenuItemClickListener(listener)
    awaitClose { setOnMenuItemClickListener(null) }
}.conflate()
