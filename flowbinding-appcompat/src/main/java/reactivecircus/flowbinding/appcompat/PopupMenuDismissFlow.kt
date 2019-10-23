package reactivecircus.flowbinding.appcompat

import androidx.annotation.CheckResult
import androidx.appcompat.widget.PopupMenu
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of dismiss events on the [PopupMenu] instance.
 *
 * Note: Created flow keeps a strong reference to the [PopupMenu] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * popupMenu.dismisses()
 *     .onEach {
 *          // handle popup menu dismiss event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun PopupMenu.dismisses(): Flow<Unit> = callbackFlow {
    checkMainThread()
    val listener = PopupMenu.OnDismissListener {
        safeOffer(Unit)
    }
    setOnDismissListener(listener)
    awaitClose { setOnDismissListener(null) }
}.conflate()
