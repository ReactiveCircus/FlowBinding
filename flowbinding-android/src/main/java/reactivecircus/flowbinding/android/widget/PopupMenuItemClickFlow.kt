package reactivecircus.flowbinding.android.widget

import android.view.MenuItem
import android.widget.PopupMenu
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of item clicked events on the [PopupMenu] instance
 * where the value emitted is the clicked menu item.
 *
 * Note: Created flow keeps a strong reference to the [PopupMenu] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * popupMenu.itemClicks()
 *     .onEach { menuItem ->
 *          // handle menuItem
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun PopupMenu.itemClicks(): Flow<MenuItem> = callbackFlow {
    checkMainThread()
    val listener = PopupMenu.OnMenuItemClickListener {
        trySend(it)
        true
    }
    setOnMenuItemClickListener(listener)
    awaitClose { setOnMenuItemClickListener(null) }
}.conflate()
