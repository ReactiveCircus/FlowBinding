package reactivecircus.flowbinding.android.widget

import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [Flow] of item click events on the [AutoCompleteTextView] instance.
 *
 * Note: Created flow keeps a strong reference to the [AutoCompleteTextView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * autoCompleteTextView.itemClickEvents()
 *     .onEach { event ->
 *          // handle auto-complete text view item click event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun AutoCompleteTextView.itemClickEvents(): Flow<AdapterViewItemClickEvent> = callbackFlow {
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
