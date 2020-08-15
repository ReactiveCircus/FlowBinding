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
 * Create a [Flow] of item click events on the [AdapterView] instance.
 *
 * Note: Created flow keeps a strong reference to the [AdapterView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * adapterView.itemClickEvents()
 *     .onEach { event ->
 *          // handle adapter view item click event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T : Adapter> AdapterView<T>.itemClickEvents(): Flow<AdapterViewItemClickEvent> = callbackFlow {
    checkMainThread()
    val listener = AdapterView.OnItemClickListener { parent, view, position, id ->
        safeOffer(
            AdapterViewItemClickEvent(
                view = parent,
                clickedView = view,
                position = position,
                id = id
            )
        )
    }
    onItemClickListener = listener
    awaitClose { onItemClickListener = null }
}.conflate()

public class AdapterViewItemClickEvent(
    public val view: AdapterView<*>,
    public val clickedView: View?,
    public val position: Int,
    public val id: Long
)
