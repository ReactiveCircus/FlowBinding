package reactivecircus.flowbinding.recyclerview

import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [InitialValueFlow] of data change events on the [RecyclerView.Adapter] instance.
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> T.dataChanges(): InitialValueFlow<T> = callbackFlow {
    checkMainThread()
    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            trySend(this@dataChanges)
        }
    }
    registerAdapterDataObserver(observer)
    awaitClose { unregisterAdapterDataObserver(observer) }
}
    .conflate()
    .asInitialValueFlow { this }
