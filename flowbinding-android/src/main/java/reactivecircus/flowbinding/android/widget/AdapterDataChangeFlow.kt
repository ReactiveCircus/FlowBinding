package reactivecircus.flowbinding.android.widget

import android.database.DataSetObserver
import android.widget.Adapter
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
 * Create a [Flow] of data change events on the [Adapter] instance.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
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
fun Adapter.dataChanges(emitImmediately: Boolean = false): Flow<Adapter> =
    callbackFlow<Adapter> {
        checkMainThread()
        val observer = object : DataSetObserver() {
            override fun onChanged() {
                safeOffer(this@dataChanges)
            }
        }
        registerDataSetObserver(observer)
        awaitClose { unregisterDataSetObserver(observer) }
    }
        .startWithCurrentValue(emitImmediately) { this }
        .conflate()
