package reactivecircus.flowbinding.appcompat

import android.view.MenuItem
import androidx.annotation.CheckResult
import androidx.appcompat.widget.ActionMenuView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of item clicked events on the [ActionMenuView] instance
 * where the value emitted is the clicked menu item.
 *
 * Note: Created flow keeps a strong reference to the [ActionMenuView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * actionMenuView.itemClicks()
 *     .onEach { menuItem ->
 *          // handle menuItem
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun ActionMenuView.itemClicks(): Flow<MenuItem> = callbackFlow {
    checkMainThread()
    val listener = ActionMenuView.OnMenuItemClickListener {
        trySend(it)
        true
    }
    setOnMenuItemClickListener(listener)
    awaitClose { setOnMenuItemClickListener(null) }
}.conflate()
