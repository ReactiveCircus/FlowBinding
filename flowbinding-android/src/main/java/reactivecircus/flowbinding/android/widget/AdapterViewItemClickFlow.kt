package reactivecircus.flowbinding.android.widget

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
 * Create a [Flow] of item clicks on the [AdapterView] instance
 * where the value emitted is the position of the item clicked.
 *
 * Note: Created flow keeps a strong reference to the [AdapterView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * adapterView.itemClickEvents()
 *     .onEach { position ->
 *          // handle adapter view item clicked
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Adapter> AdapterView<T>.itemClicks(): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = AdapterView.OnItemClickListener { _, _, position, _ ->
        safeOffer(position)
    }
    onItemClickListener = listener
    awaitClose { onItemClickListener = null }
}.conflate()
