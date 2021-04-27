@file:Suppress("MatchingDeclarationName")

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
 * Create a [InitialValueFlow] of query text events on the [SearchView] instance
 * where the value emitted is one of the 2 event types:
 * [QueryTextEvent.QueryChanged],
 * [QueryTextEvent.QuerySubmitted]
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
@OptIn(ExperimentalCoroutinesApi::class)
public fun SearchView.queryTextEvents(): InitialValueFlow<QueryTextEvent> = callbackFlow {
    checkMainThread()
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            trySend(
                QueryTextEvent.QueryChanged(
                    view = this@queryTextEvents,
                    queryText = newText
                )
            )
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            trySend(
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
    .conflate()
    .asInitialValueFlow {
        QueryTextEvent.QueryChanged(
            view = this,
            queryText = query
        )
    }

public sealed class QueryTextEvent {
    public abstract val view: SearchView
    public abstract val queryText: CharSequence

    public data class QueryChanged(
        override val view: SearchView,
        override val queryText: CharSequence
    ) : QueryTextEvent()

    public data class QuerySubmitted(
        override val view: SearchView,
        override val queryText: CharSequence
    ) : QueryTextEvent()
}
