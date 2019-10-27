package reactivecircus.flowbinding.recyclerview

import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of data change events on the [RecyclerView.Adapter] instance.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [RecyclerView.Adapter] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * adapter.dataChanges()
 *     .onEach { adapter ->
 *          // handle data changed
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> T.dataChanges(emitImmediately: Boolean = false): Flow<T> =
    callbackFlow<T> {
        checkMainThread()
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                safeOffer(this@dataChanges)
            }
        }
        registerAdapterDataObserver(observer)
        awaitClose { unregisterAdapterDataObserver(observer) }
    }
        .startWithCurrentValue(emitImmediately) { this }
        .conflate()
