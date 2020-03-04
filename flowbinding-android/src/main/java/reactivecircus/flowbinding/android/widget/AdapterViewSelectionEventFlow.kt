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
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of item selection events on the [AdapterView] instance
 * where the value emitted is one of the 2 event types:
 * [AdapterViewSelectionEvent.ItemSelected],
 * [AdapterViewSelectionEvent.NothingSelected]
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [AdapterView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * adapterView.selectionEvents()
 *     .onEach { event ->
 *          // handle adapter view selection event
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Adapter> AdapterView<T>.selectionEvents(emitImmediately: Boolean = false): Flow<AdapterViewSelectionEvent> =
    callbackFlow<AdapterViewSelectionEvent> {
        checkMainThread()
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                safeOffer(
                    AdapterViewSelectionEvent.ItemSelected(
                        view = parent,
                        selectedView = view,
                        position = position,
                        id = id
                    )
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                safeOffer(
                    AdapterViewSelectionEvent.NothingSelected(view = parent)
                )
            }
        }
        onItemSelectedListener = listener
        awaitClose { onItemSelectedListener = null }
    }
        .startWithCurrentValue(emitImmediately) {
            val selectedPosition = selectedItemPosition
            if (selectedPosition == AdapterView.INVALID_POSITION) {
                AdapterViewSelectionEvent.NothingSelected(view = this)
            } else {
                AdapterViewSelectionEvent.ItemSelected(
                    view = this,
                    selectedView = selectedView,
                    position = selectedPosition,
                    id = selectedItemId
                )
            }
        }
        .conflate()

sealed class AdapterViewSelectionEvent {
    abstract val view: AdapterView<*>

    class ItemSelected(
        override val view: AdapterView<*>,
        val selectedView: View?,
        val position: Int,
        val id: Long
    ) : AdapterViewSelectionEvent()

    class NothingSelected(override val view: AdapterView<*>) : AdapterViewSelectionEvent()
}
