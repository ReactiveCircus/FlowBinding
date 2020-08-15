package reactivecircus.flowbinding.android.widget

import android.database.DataSetObserver
import android.widget.Adapter
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
 * Create a [InitialValueFlow] of data change events on the [Adapter] instance.
 *
 * Note: Created flow keeps a strong reference to the [Adapter] instance
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
public fun Adapter.dataChanges(): InitialValueFlow<Adapter> = callbackFlow<Adapter> {
    checkMainThread()
    val observer = object : DataSetObserver() {
        override fun onChanged() {
            safeOffer(this@dataChanges)
        }
    }
    registerDataSetObserver(observer)
    awaitClose { unregisterDataSetObserver(observer) }
}
    .conflate()
    .asInitialValueFlow { this }
