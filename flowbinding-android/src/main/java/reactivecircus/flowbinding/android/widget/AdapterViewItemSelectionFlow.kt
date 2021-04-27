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

/**
 * Create a [InitialValueFlow] of item selections on the [AdapterView] instance
 * where the value emitted is the selected position, or [AdapterView.INVALID_POSITION] if nothing is selected.
 *
 * Note: Created flow keeps a strong reference to the [AdapterView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * adapterView.itemSelections()
 *     .onEach { position ->
 *          // handle adapter view item selection
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T : Adapter> AdapterView<T>.itemSelections(): InitialValueFlow<Int> = callbackFlow {
    checkMainThread()
    val listener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            trySend(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            trySend(AdapterView.INVALID_POSITION)
        }
    }
    onItemSelectedListener = listener
    awaitClose { onItemSelectedListener = null }
}
    .conflate()
    .asInitialValueFlow { selectedItemPosition }
