@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.appcompat

import androidx.annotation.CheckResult
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread
import reactivecircus.flowbinding.common.safeOffer
import reactivecircus.flowbinding.common.startWithCurrentValue

/**
 * Create a [Flow] of query text events on the [SearchView] instance
 * where the value emitted is one of the 2 event types:
 * [QueryTextEvent.QueryChanged],
 * [QueryTextEvent.QuerySubmitted]
 *
 * @param emitImmediately whether to emit the current value (if any) immediately on flow collection.
 *
 * Note: Created flow keeps a strong reference to the [SearchView] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * searchView.queryTextEvents()
 *     .onEach { queryTextEvent ->
 *          // handle search view query text cha
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@UseExperimental(ExperimentalCoroutinesApi::class)
fun SearchView.queryTextEvents(emitImmediately: Boolean = false): Flow<QueryTextEvent> = callbackFlow<QueryTextEvent> {
    checkMainThread()
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            safeOffer(
                QueryTextEvent.QueryChanged(
                    view = this@queryTextEvents,
                    queryText = newText
                )
            )
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            safeOffer(
                QueryTextEvent.QuerySubmitted(
                    view = this@queryTextEvents,
                    queryText = query
                )
            )
            return true
        }
    }
    setOnQueryTextListener(listener)
    awaitClose { setOnQueryTextListener(null) }
}
    .startWithCurrentValue(emitImmediately) {
        QueryTextEvent.QueryChanged(
            view = this,
            queryText = query
        )
    }
    .conflate()

sealed class QueryTextEvent {
    abstract val view: SearchView
    abstract val queryText: CharSequence

    class QueryChanged(
        override val view: SearchView,
        override val queryText: CharSequence
    ) : QueryTextEvent()

    class QuerySubmitted(
        override val view: SearchView,
        override val queryText: CharSequence
    ) : QueryTextEvent()
}
