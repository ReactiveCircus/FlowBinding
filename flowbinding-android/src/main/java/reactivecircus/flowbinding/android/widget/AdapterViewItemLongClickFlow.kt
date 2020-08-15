package reactivecircus.flowbinding.android.widget

import android.widget.Adapter
import android.widget.AdapterView
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of item long clicks on the [AdapterView] instance
 * where the value emitted is the position of the item clicked.
 *
 * @param handled function to be invoked with each value to determine the return value of
 * the underlying [AdapterView.OnItemLongClickListener]. Note that the [Flow] will only emit when this function
 * evaluates to true.
 *
 * Note: Created flow keeps a strong reference to the [AdapterView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * adapterView.itemLongClicks { it.position == 0 }
 *     .onEach { position ->
 *          // handle adapter view item long clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T : Adapter> AdapterView<T>.itemLongClicks(
    handled: () -> Boolean = { true }
): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
        if (handled()) {
            safeOffer(position)
            true
        } else {
            false
        }
    }
    onItemLongClickListener = listener
    awaitClose { onItemLongClickListener = null }
}.conflate()
