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
 * Create a [Flow] of item selections on the [AdapterView] instance
 * where the value emitted is the selected position, or [AdapterView.INVALID_POSITION] if nothing is selected.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
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
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T : Adapter> AdapterView<T>.itemSelections(emitImmediately: Boolean = false): Flow<Int> =
    callbackFlow<Int> {
        checkMainThread()
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                safeOffer(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                safeOffer(AdapterView.INVALID_POSITION)
            }
        }
        onItemSelectedListener = listener
        awaitClose { onItemSelectedListener = null }
    }
        .startWithCurrentValue(emitImmediately) { selectedItemPosition }
        .conflate()
