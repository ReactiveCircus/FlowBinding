@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.view.View
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
 * Create a [Flow] of item long click events on the [AdapterView] instance.
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
 * adapterView.itemLongClickEvents { it.position == 0 }
 *     .onEach { event ->
 *          // handle adapter view item long click event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T : Adapter> AdapterView<T>.itemLongClickEvents(
    handled: (AdapterViewItemLongClickEvent) -> Boolean = { true }
): Flow<AdapterViewItemLongClickEvent> = callbackFlow {
    checkMainThread()
    val listener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
        val event = AdapterViewItemLongClickEvent(
            view = parent,
            longClickedView = view,
            position = position,
            id = id
        )
        if (handled(event)) {
            safeOffer(event)
            true
        } else {
            false
        }
    }
    onItemLongClickListener = listener
    awaitClose { onItemLongClickListener = null }
}.conflate()

public class AdapterViewItemLongClickEvent(
    public val view: AdapterView<*>,
    public val longClickedView: View?,
    public val position: Int,
    public val id: Long
)
