package reactivecircus.flowbinding.appcompat

import android.view.MenuItem
import androidx.annotation.CheckResult
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of item clicked events on the [Toolbar] instance
 * where the value emitted is the clicked menu item.
 *
 * Note: Created flow keeps a strong reference to the [Toolbar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * toolbar.itemClicks()
 *     .onEach { menuItem ->
 *          // handle menuItem
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun Toolbar.itemClicks(): Flow<MenuItem> = callbackFlow {
    checkMainThread()
    val listener = Toolbar.OnMenuItemClickListener {
        safeOffer(it)
    }
    setOnMenuItemClickListener(listener)
    awaitClose { setOnMenuItemClickListener(null) }
}.conflate()
