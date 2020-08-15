package reactivecircus.flowbinding.appcompat

import android.view.View
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
 * Create a [Flow] of navigation click events on the [Toolbar] instance.
 *
 * Note: Created flow keeps a strong reference to the [Toolbar] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * popupMenu.dismisses()
 *     .onEach {
 *          // handle toolbar navigation click event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun Toolbar.navigationClicks(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = View.OnClickListener {
        safeOffer(Unit)
    }
    setNavigationOnClickListener(listener)
    awaitClose { setNavigationOnClickListener(null) }
}.conflate()
