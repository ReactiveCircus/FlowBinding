@file:Suppress("MatchingDeclarationName")

package reactivecircus.flowbinding.preference

import androidx.annotation.CheckResult
import androidx.preference.Preference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import reactivecircus.flowbinding.common.checkMainThread

/**
 * Create a [Flow] of change events on the [Preference] instance
 * where the value emitted is the new value of the [Preference].
 *
 * Note: Created flow keeps a strong reference to the [Preference] instance
 * until the coroutine that launched the flow collector is cancelled.
 *
 * Example of usage:
 *
 * ```
 * preference.preferenceClicks()
 *     .onEach { value ->
 *          // handle value
 *     }
 *     .launchIn(uiScope)
 * ```
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
public fun Preference.preferenceChanges(): Flow<Any> = callbackFlow {
    checkMainThread()
    val listener = Preference.OnPreferenceChangeListener { _, newValue ->
        trySend(newValue)
        true
    }
    onPreferenceChangeListener = listener
    awaitClose { onPreferenceChangeListener = null }
}.conflate()
