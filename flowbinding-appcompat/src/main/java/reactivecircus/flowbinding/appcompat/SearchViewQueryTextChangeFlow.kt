package reactivecircus.flowbinding.appcompat

import androidx.annotation.CheckResult
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.InitialValueFlow
import reactivecircus.flowbinding.common.asInitialValueFlow
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [InitialValueFlow] of query text changes on the [SearchView] instance
 * where the value emitted is latest query text.
 *
 * Note: Created flow keeps a strong reference to the [SearchView] instance
 * until the coroutine that launched the flow collector is cancelled.
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun SearchView.queryTextChanges(): InitialValueFlow<CharSequence> = callbackFlow {
    checkMainThread()
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            trySend(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean = false
    }
    setOnQueryTextListener(listener)
    awaitClose { setOnQueryTextListener(null) }
}
    .conflate()
    .asInitialValueFlow { query }
