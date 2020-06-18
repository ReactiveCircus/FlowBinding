@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.android.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer

/**
 * Create a [InitialValueFlow] of item selection events on the [AdapterView] instance
 * where the value emitted is one of the 2 event types:
 * [AdapterViewSelectionEvent.ItemSelected],
 * [AdapterViewSelectionEvent.NothingSelected]
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
fun <T : Adapter> AdapterView<T>.selectionEvents(): InitialValueFlow<AdapterViewSelectionEvent> =
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
        .conflate()
        .asInitialValueFlow {
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
