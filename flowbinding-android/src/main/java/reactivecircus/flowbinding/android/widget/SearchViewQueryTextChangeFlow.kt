package reactivecircus.flowbinding.android.widget

import android.widget.SearchView
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
 * Create a [Flow] of query text changes on the [SearchView] instance
 * where the value emitted is latest query text.
 *
 * Note: Created flow keeps a strong reference to the [SearchView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Example of usage:
 *
 * ```
 * searchView.queryTextChanges()
 *     .onEach { queryText ->
 *          // handle queryText
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchView.queryTextChanges(emitImmediately: Boolean = false): Flow<CharSequence> = callbackFlow<CharSequence> {
    checkMainThread()
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            safeOffer(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean = false
    }
    setOnQueryTextListener(listener)
    awaitClose { setOnQueryTextListener(null) }
}
    .startWithCurrentValue(emitImmediately) { query }
    .conflate()
